package com.soa.auth.controller;

import com.soa.auth.dto.ApiResponse;
import com.soa.auth.dto.AuthDTOs.*;
import com.soa.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse res = authService.login(request);
            return ResponseEntity.ok(ApiResponse.ok("Đăng nhập thành công", res));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {
        try {
            AuthResponse res = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Đăng ký tài khoản thành công", res));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/doi-mat-khau")
    public ResponseEntity<ApiResponse<Void>> doiMatKhau(@RequestBody DoiMatKhauRequest request) {
        try {
            authService.doiMatKhau(request);
            return ResponseEntity.ok(ApiResponse.ok("Đổi mật khẩu thành công", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/quen-mat-khau")
    public ResponseEntity<ApiResponse<Void>> quenMatKhau(@RequestBody QuenMatKhauRequest request) {
        try {
            authService.quenMatKhau(request);
            return ResponseEntity.ok(ApiResponse.ok("Đã gửi hướng dẫn đặt lại mật khẩu về email", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }
}
