package com.soa.auth.service;

import com.soa.auth.dto.AuthDTOs.*;
import com.soa.auth.entity.TaiKhoan;
import com.soa.auth.repository.TaiKhoanRepository;
import com.soa.auth.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private TaiKhoanRepository taiKhoanRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginSuccessReturnsToken() {
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap("sv001");
        tk.setMatKhau("encoded");
        tk.setHoTen("Nguyen Van A");
        tk.setVaiTro("SINH_VIEN");
        tk.setMaDinhDanh("SV001");

        when(taiKhoanRepository.findByTenDangNhap("sv001")).thenReturn(Optional.of(tk));
        when(passwordEncoder.matches("123456", "encoded")).thenReturn(true);
        when(jwtService.generateToken("sv001", "SINH_VIEN", "Nguyen Van A", "SV001")).thenReturn("jwt-token");

        AuthResponse res = authService.login(new LoginRequest("sv001", "123456"));
        assertEquals("jwt-token", res.getToken());
        assertEquals("SINH_VIEN", res.getVaiTro());
    }

    @Test
    void quenMatKhauGeneratesOtpWithoutOverwritingPassword() {
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap("sv001");
        tk.setMatKhau("original-hash");
        tk.setHoTen("Nguyen Van A");
        tk.setEmail("sv001@qnu.edu.vn");

        when(taiKhoanRepository.findByEmail("sv001@qnu.edu.vn")).thenReturn(Optional.of(tk));

        QuenMatKhauResponse res = authService.quenMatKhau(new QuenMatKhauRequest("sv001@qnu.edu.vn"));

        assertEquals("sv001@qnu.edu.vn", res.getEmail());
        assertEquals("original-hash", tk.getMatKhau());
        assertNotNull(tk.getResetOtp());
        assertEquals(6, tk.getResetOtp().length());
        assertNotNull(tk.getResetOtpExpiry());
        verify(emailService).sendPasswordResetOtpEmail(eq("sv001@qnu.edu.vn"), eq("Nguyen Van A"), eq(tk.getResetOtp()));
    }

    @Test
    void datLaiMatKhauVerifiesOtpAndClearsToken() {
        TaiKhoan tk = new TaiKhoan();
        tk.setEmail("sv001@qnu.edu.vn");
        tk.setResetOtp("123456");
        tk.setResetOtpExpiry(LocalDateTime.now().plusMinutes(10));

        when(taiKhoanRepository.findByEmail("sv001@qnu.edu.vn")).thenReturn(Optional.of(tk));
        when(passwordEncoder.encode("NewPass@123")).thenReturn("new-hash");

        XacNhanDatLaiMatKhauRequest req = new XacNhanDatLaiMatKhauRequest();
        req.setEmail("sv001@qnu.edu.vn");
        req.setOtp("123456");
        req.setMatKhauMoi("NewPass@123");

        authService.datLaiMatKhau(req);

        assertEquals("new-hash", tk.getMatKhau());
        assertNull(tk.getResetOtp());
        assertNull(tk.getResetOtpExpiry());
        verify(taiKhoanRepository).save(tk);
    }
}
