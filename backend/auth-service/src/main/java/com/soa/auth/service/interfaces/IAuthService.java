package com.soa.auth.service.interfaces;

import com.soa.auth.dto.AuthDTOs.*;

public interface IAuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
    void doiMatKhau(DoiMatKhauRequest request);
    QuenMatKhauResponse quenMatKhau(QuenMatKhauRequest request);
}
