package com.soa.auth.dto;

public class AuthDTOs {

    public static class LoginRequest {
        private String tenDangNhap;
        private String matKhau;

        public LoginRequest() {}
        public LoginRequest(String tenDangNhap, String matKhau) {
            this.tenDangNhap = tenDangNhap;
            this.matKhau = matKhau;
        }

        public String getTenDangNhap() { return tenDangNhap; }
        public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

        public String getMatKhau() { return matKhau; }
        public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    }

    public static class RegisterRequest {
        private String tenDangNhap;
        private String matKhau;
        private String hoTen;
        private String email;
        private String maSinhVien;

        public RegisterRequest() {}

        public String getTenDangNhap() { return tenDangNhap; }
        public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

        public String getMatKhau() { return matKhau; }
        public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

        public String getHoTen() { return hoTen; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getMaSinhVien() { return maSinhVien; }
        public void setMaSinhVien(String maSinhVien) { this.maSinhVien = maSinhVien; }
    }

    public static class AuthResponse {
        private String token;
        private String tokenType = "Bearer";
        private String tenDangNhap;
        private String hoTen;
        private String vaiTro;
        private String maDinhDanh;
        private long expiresIn;

        public AuthResponse() {}

        public AuthResponse(String token, String tokenType, String tenDangNhap, String hoTen, String vaiTro, String maDinhDanh, long expiresIn) {
            this.token = token;
            this.tokenType = tokenType != null ? tokenType : "Bearer";
            this.tenDangNhap = tenDangNhap;
            this.hoTen = hoTen;
            this.vaiTro = vaiTro;
            this.maDinhDanh = maDinhDanh;
            this.expiresIn = expiresIn;
        }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public String getTokenType() { return tokenType; }
        public void setTokenType(String tokenType) { this.tokenType = tokenType; }

        public String getTenDangNhap() { return tenDangNhap; }
        public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

        public String getHoTen() { return hoTen; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }

        public String getVaiTro() { return vaiTro; }
        public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }

        public String getMaDinhDanh() { return maDinhDanh; }
        public void setMaDinhDanh(String maDinhDanh) { this.maDinhDanh = maDinhDanh; }

        public long getExpiresIn() { return expiresIn; }
        public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
    }

    public static class DoiMatKhauRequest {
        private String tenDangNhap;
        private String matKhauCu;
        private String matKhauMoi;

        public DoiMatKhauRequest() {}

        public String getTenDangNhap() { return tenDangNhap; }
        public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }

        public String getMatKhauCu() { return matKhauCu; }
        public void setMatKhauCu(String matKhauCu) { this.matKhauCu = matKhauCu; }

        public String getMatKhauMoi() { return matKhauMoi; }
        public void setMatKhauMoi(String matKhauMoi) { this.matKhauMoi = matKhauMoi; }
    }

}
