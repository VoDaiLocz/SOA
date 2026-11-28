package com.soa.auth.service;

import com.soa.auth.client.UserClient;
import com.soa.auth.client.UserClient.SinhVienSyncDTO;
import com.soa.auth.dto.AuthDTOs.*;
import com.soa.auth.entity.TaiKhoan;
import com.soa.auth.repository.TaiKhoanRepository;
import com.soa.auth.security.JwtService;
import com.soa.auth.service.interfaces.IAuthService;
import feign.FeignException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthService implements IAuthService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final UserClient userClient;

    public AuthService(TaiKhoanRepository taiKhoanRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       EmailService emailService,
                       UserClient userClient) {
        this.taiKhoanRepository = taiKhoanRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.userClient = userClient;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        TaiKhoan tk = taiKhoanRepository.findByTenDangNhap(request.getTenDangNhap())
                .orElseThrow(() -> new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không chính xác"));

        if (!passwordEncoder.matches(request.getMatKhau(), tk.getMatKhau())) {
            throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không chính xác");
        }

        String token = jwtService.generateToken(tk.getTenDangNhap(), tk.getVaiTro(), tk.getHoTen(), tk.getMaDinhDanh());
        return new AuthResponse(token, "Bearer", tk.getTenDangNhap(), tk.getHoTen(), tk.getVaiTro(), tk.getMaDinhDanh(), 3600);
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        validateRegisterRequest(request);

        String maSv = request.getMaSinhVien().trim();
        String email = request.getEmail().trim();

        if (taiKhoanRepository.existsByTenDangNhap(request.getTenDangNhap().trim())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại trong hệ thống");
        }
        if (taiKhoanRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email đã được sử dụng bởi tài khoản khác");
        }
        if (taiKhoanRepository.existsByMaDinhDanh(maSv)) {
            throw new IllegalArgumentException("Mã sinh viên này đã có tài khoản trong hệ thống");
        }

        ensureStudentProfileExists(maSv, request.getHoTen().trim(), email);

        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(request.getTenDangNhap().trim());
        tk.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        tk.setHoTen(request.getHoTen().trim());
        tk.setEmail(email);
        tk.setVaiTro("SINH_VIEN");
        tk.setMaDinhDanh(maSv);
        tk.setTrangThai("HOAT_DONG");

        taiKhoanRepository.save(tk);

        String token = jwtService.generateToken(tk.getTenDangNhap(), tk.getVaiTro(), tk.getHoTen(), tk.getMaDinhDanh());
        return new AuthResponse(token, "Bearer", tk.getTenDangNhap(), tk.getHoTen(), tk.getVaiTro(), tk.getMaDinhDanh(), 3600);
    }

    private void validateRegisterRequest(RegisterRequest req) {
        if (req == null || isBlank(req.getTenDangNhap()) || isBlank(req.getMatKhau())
                || isBlank(req.getHoTen()) || isBlank(req.getEmail()) || isBlank(req.getMaSinhVien())) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ tên đăng nhập, mật khẩu, họ tên, email và mã sinh viên");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void ensureStudentProfileExists(String maSv, String hoTen, String email) {
        try {
            userClient.getSinhVienByMa(maSv);
        } catch (FeignException.NotFound e) {
            // Tự động đồng bộ tạo mới hồ sơ sinh viên bên user-service nếu chưa có
            userClient.createSinhVien(new SinhVienSyncDTO(maSv, hoTen, email, "DANG_HOC"));
        } catch (Exception e) {
            throw new IllegalStateException("Không thể đồng bộ hồ sơ sinh viên với user-service: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void doiMatKhau(DoiMatKhauRequest request) {
        TaiKhoan tk = taiKhoanRepository.findByTenDangNhap(request.getTenDangNhap())
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản không tồn tại"));

        if (!passwordEncoder.matches(request.getMatKhauCu(), tk.getMatKhau())) {
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác");
        }

        tk.setMatKhau(passwordEncoder.encode(request.getMatKhauMoi()));
        taiKhoanRepository.save(tk);
    }

    @Override
    @Transactional
    public QuenMatKhauResponse quenMatKhau(QuenMatKhauRequest request) {
        String identifier = request.getEmail() != null && !request.getEmail().trim().isEmpty() 
                ? request.getEmail().trim() 
                : (request.getTenDangNhap() != null ? request.getTenDangNhap().trim() : null);

        if (identifier == null || identifier.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp địa chỉ email hoặc tên đăng nhập");
        }

        TaiKhoan tk = taiKhoanRepository.findByEmail(identifier)
                .or(() -> taiKhoanRepository.findByTenDangNhap(identifier))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với: " + identifier));

        if (tk.getEmail() == null || tk.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Tài khoản này chưa có email liên kết để nhận OTP");
        }

        String email = tk.getEmail().trim();
        String otp = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        tk.setResetOtp(otp);
        tk.setResetOtpExpiry(LocalDateTime.now().plusMinutes(15));
        taiKhoanRepository.save(tk);

        boolean hasSmtpConfig = emailService.hasSmtpConfig();
        emailService.sendPasswordResetOtpEmail(email, tk.getHoTen(), otp);

        String thongBao = hasSmtpConfig
                ? "Mã xác nhận (OTP) có hiệu lực 15 phút đã được gửi tới email " + email
                : "Mã OTP xác nhận (chế độ Test): [" + otp + "] - Hãy cấu hình Gmail SMTP trong docker-compose để gửi email thật tới " + email;

        return new QuenMatKhauResponse(email, thongBao);
    }

    @Override
    @Transactional
    public void datLaiMatKhau(XacNhanDatLaiMatKhauRequest request) {
        if (request.getEmail() == null || request.getOtp() == null || request.getMatKhauMoi() == null
                || request.getMatKhauMoi().isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ email hoặc tên đăng nhập, mã OTP và mật khẩu mới");
        }

        String identifier = request.getEmail().trim();
        TaiKhoan tk = taiKhoanRepository.findByEmail(identifier)
                .or(() -> taiKhoanRepository.findByTenDangNhap(identifier))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với: " + identifier));

        if (tk.getResetOtp() == null || tk.getResetOtpExpiry() == null
                || LocalDateTime.now().isAfter(tk.getResetOtpExpiry())) {
            throw new IllegalArgumentException("Mã OTP đã hết hạn hoặc chưa được yêu cầu");
        }
        if (!tk.getResetOtp().equals(request.getOtp().trim())) {
            throw new IllegalArgumentException("Mã OTP không chính xác");
        }

        tk.setMatKhau(passwordEncoder.encode(request.getMatKhauMoi()));
        tk.setResetOtp(null);
        tk.setResetOtpExpiry(null);
        taiKhoanRepository.save(tk);
    }
}
