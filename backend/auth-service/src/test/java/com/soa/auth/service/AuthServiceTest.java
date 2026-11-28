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
    void quenMatKhauEncodesTempPasswordAndSendsEmail() {
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap("sv001");
        tk.setHoTen("Nguyen Van A");
        tk.setEmail("sv001@qnu.edu.vn");

        when(taiKhoanRepository.findByEmail("sv001@qnu.edu.vn")).thenReturn(Optional.of(tk));
        when(passwordEncoder.encode(anyString())).thenReturn("new-encoded");

        QuenMatKhauResponse res = authService.quenMatKhau(new QuenMatKhauRequest("sv001@qnu.edu.vn"));

        assertEquals("sv001@qnu.edu.vn", res.getEmail());
        assertNotNull(res.getThongBao());
        assertEquals("new-encoded", tk.getMatKhau());
        verify(taiKhoanRepository).save(tk);
        verify(emailService).sendPasswordResetEmail(eq("sv001@qnu.edu.vn"), eq("Nguyen Van A"), anyString());
    }
}
