package com.soa.storage.dto;

import java.time.LocalDateTime;

public class StorageDTOs {

    public static class TaiLieuDTO {
        private Long id;
        private String maDeTai;
        private String tenFile;
        private String duongDan;
        private String loaiTaiLieu;
        private Long kichThuoc;
        private String nguoiUpload;
        private LocalDateTime ngayUpload;

        public TaiLieuDTO() {}

        public TaiLieuDTO(Long id, String maDeTai, String tenFile, String duongDan, String loaiTaiLieu, Long kichThuoc, String nguoiUpload, LocalDateTime ngayUpload) {
            this.id = id;
            this.maDeTai = maDeTai;
            this.tenFile = tenFile;
            this.duongDan = duongDan;
            this.loaiTaiLieu = loaiTaiLieu;
            this.kichThuoc = kichThuoc;
            this.nguoiUpload = nguoiUpload;
            this.ngayUpload = ngayUpload;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getMaDeTai() { return maDeTai; }
        public void setMaDeTai(String maDeTai) { this.maDeTai = maDeTai; }
        public String getTenFile() { return tenFile; }
        public void setTenFile(String tenFile) { this.tenFile = tenFile; }
        public String getDuongDan() { return duongDan; }
        public void setDuongDan(String duongDan) { this.duongDan = duongDan; }
        public String getLoaiTaiLieu() { return loaiTaiLieu; }
        public void setLoaiTaiLieu(String loaiTaiLieu) { this.loaiTaiLieu = loaiTaiLieu; }
        public Long getKichThuoc() { return kichThuoc; }
        public void setKichThuoc(Long kichThuoc) { this.kichThuoc = kichThuoc; }
        public String getNguoiUpload() { return nguoiUpload; }
        public void setNguoiUpload(String nguoiUpload) { this.nguoiUpload = nguoiUpload; }
        public LocalDateTime getNgayUpload() { return ngayUpload; }
        public void setNgayUpload(LocalDateTime ngayUpload) { this.ngayUpload = ngayUpload; }
    }
}
