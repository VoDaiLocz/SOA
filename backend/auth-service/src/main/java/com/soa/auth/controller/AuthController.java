package com.soa.auth.controller;

import com.soa.auth.dto.ApiResponse;
import com.soa.auth.dto.AuthDTOs.*;
import com.soa.auth.service.interfaces.IAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Đăng nhập thành công", authService.login(request)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Đăng ký tài khoản thành công", authService.register(request)));
    }

    @PostMapping("/doi-mat-khau")
    public ResponseEntity<ApiResponse<Void>> doiMatKhau(@RequestBody DoiMatKhauRequest request) {
        authService.doiMatKhau(request);
        return ResponseEntity.ok(ApiResponse.ok("Đổi mật khẩu thành công", null));
    }

    @PostMapping("/quen-mat-khau")
    public ResponseEntity<ApiResponse<QuenMatKhauResponse>> quenMatKhau(@RequestBody QuenMatKhauRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Mã OTP đặt lại mật khẩu đã được gửi qua email", authService.quenMatKhau(request)));
    }

    @PostMapping("/dat-lai-mat-khau")
    public ResponseEntity<ApiResponse<Void>> datLaiMatKhau(@RequestBody XacNhanDatLaiMatKhauRequest request) {
        authService.datLaiMatKhau(request);
        return ResponseEntity.ok(ApiResponse.ok("Đặt lại mật khẩu thành công", null));
    }
}
