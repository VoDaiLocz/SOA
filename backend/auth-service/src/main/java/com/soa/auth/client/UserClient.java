package com.soa.auth.client;

import com.soa.auth.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${app.services.user-url:http://localhost:8081}")
public interface UserClient {

    @GetMapping("/api/v1/sinhvien/{maSv}")
    ApiResponse<SinhVienSyncDTO> getSinhVienByMa(@PathVariable("maSv") String maSv);

    @PostMapping("/api/v1/sinhvien")
    ApiResponse<SinhVienSyncDTO> createSinhVien(@RequestBody SinhVienSyncDTO dto);

    class SinhVienSyncDTO {
        private String maSv;
        private String hoTen;
        private String email;
        private String trangThai;

        public SinhVienSyncDTO() {}

        public SinhVienSyncDTO(String maSv, String hoTen, String email, String trangThai) {
            this.maSv = maSv;
            this.hoTen = hoTen;
            this.email = email;
            this.trangThai = trangThai;
        }

        public String getMaSv() { return maSv; }
        public void setMaSv(String maSv) { this.maSv = maSv; }
        public String getHoTen() { return hoTen; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getTrangThai() { return trangThai; }
        public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    }
}
