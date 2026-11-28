package com.soa.auth.service;

import com.soa.auth.dto.AuthDTOs.*;
import com.soa.auth.entity.TaiKhoan;
import com.soa.auth.repository.TaiKhoanRepository;
import com.soa.auth.security.JwtService;
import com.soa.auth.service.interfaces.IAuthService;
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

    public AuthService(TaiKhoanRepository taiKhoanRepository, PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService) {
        this.taiKhoanRepository = taiKhoanRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
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
        if (taiKhoanRepository.existsByTenDangNhap(request.getTenDangNhap())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại trong hệ thống");
        }

        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(request.getTenDangNhap());
        tk.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        tk.setHoTen(request.getHoTen());
        tk.setEmail(request.getEmail());
        tk.setVaiTro("SINH_VIEN");
        tk.setMaDinhDanh(request.getMaSinhVien());
        tk.setTrangThai("HOAT_DONG");

        taiKhoanRepository.save(tk);

        String token = jwtService.generateToken(tk.getTenDangNhap(), tk.getVaiTro(), tk.getHoTen(), tk.getMaDinhDanh());

        return new AuthResponse(token, "Bearer", tk.getTenDangNhap(), tk.getHoTen(), tk.getVaiTro(), tk.getMaDinhDanh(), 3600);
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
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp địa chỉ email liên kết");
        }

        String email = request.getEmail().trim();
        TaiKhoan tk = taiKhoanRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản liên kết với email: " + email));

        // Sinh mã OTP 6 chữ số có thời hạn 15 phút (không ghi đè mật khẩu trước khi xác thực OTP)
        String otp = String.format("%06d", new SecureRandom().nextInt(1_000_000));
        tk.setResetOtp(otp);
        tk.setResetOtpExpiry(LocalDateTime.now().plusMinutes(15));
        taiKhoanRepository.save(tk);

        emailService.sendPasswordResetOtpEmail(email, tk.getHoTen(), otp);

        return new QuenMatKhauResponse(
                email,
                "Mã xác nhận (OTP) có hiệu lực 15 phút đã được gửi tới email " + email
        );
    }

    @Override
    @Transactional
    public void datLaiMatKhau(XacNhanDatLaiMatKhauRequest request) {
        if (request.getEmail() == null || request.getOtp() == null || request.getMatKhauMoi() == null
                || request.getMatKhauMoi().isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ email, mã OTP và mật khẩu mới");
        }

        TaiKhoan tk = taiKhoanRepository.findByEmail(request.getEmail().trim())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản với email này"));

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
