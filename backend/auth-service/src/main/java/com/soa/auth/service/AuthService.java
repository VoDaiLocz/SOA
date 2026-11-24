package com.soa.auth.service;

import com.soa.auth.dto.AuthDTOs.*;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
    void doiMatKhau(DoiMatKhauRequest request);
    void quenMatKhau(QuenMatKhauRequest request);
}
