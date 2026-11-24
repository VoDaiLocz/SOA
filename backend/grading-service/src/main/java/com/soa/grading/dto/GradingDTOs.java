package com.soa.grading.dto;

import java.time.LocalDateTime;

public class GradingDTOs {

    public static class DiemDTO {
        private Long id;
        private String maDeTai;
        private String maGv;
        private String tenGv;
        private String loaiDiem; // DE_CUONG, BAO_VE
        private Double diemSo;
        private String nhanXet;
        private LocalDateTime ngayCham;

        public DiemDTO() {}

        public DiemDTO(Long id, String maDeTai, String maGv, String tenGv, String loaiDiem, Double diemSo, String nhanXet, LocalDateTime ngayCham) {
            this.id = id;
            this.maDeTai = maDeTai;
            this.maGv = maGv;
            this.tenGv = tenGv;
            this.loaiDiem = loaiDiem;
            this.diemSo = diemSo;
            this.nhanXet = nhanXet;
            this.ngayCham = ngayCham;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getMaDeTai() { return maDeTai; }
        public void setMaDeTai(String maDeTai) { this.maDeTai = maDeTai; }
        public String getMaGv() { return maGv; }
        public void setMaGv(String maGv) { this.maGv = maGv; }
        public String getTenGv() { return tenGv; }
        public void setTenGv(String tenGv) { this.tenGv = tenGv; }
        public String getLoaiDiem() { return loaiDiem; }
        public void setLoaiDiem(String loaiDiem) { this.loaiDiem = loaiDiem; }
        public Double getDiemSo() { return diemSo; }
        public void setDiemSo(Double diemSo) { this.diemSo = diemSo; }
        public String getNhanXet() { return nhanXet; }
        public void setNhanXet(String nhanXet) { this.nhanXet = nhanXet; }
        public LocalDateTime getNgayCham() { return ngayCham; }
        public void setNgayCham(LocalDateTime ngayCham) { this.ngayCham = ngayCham; }
    }

    public static class NhapDiemRequest {
        private String maDeTai;
        private String maGv;
        private String tenGv;
        private String loaiDiem;
        private Double diemSo;
        private String nhanXet;

        public NhapDiemRequest() {}

        public NhapDiemRequest(String maDeTai, String maGv, String tenGv, String loaiDiem, Double diemSo, String nhanXet) {
            this.maDeTai = maDeTai;
            this.maGv = maGv;
            this.tenGv = tenGv;
            this.loaiDiem = loaiDiem;
            this.diemSo = diemSo;
            this.nhanXet = nhanXet;
        }

        public String getMaDeTai() { return maDeTai; }
        public void setMaDeTai(String maDeTai) { this.maDeTai = maDeTai; }
        public String getMaGv() { return maGv; }
        public void setMaGv(String maGv) { this.maGv = maGv; }
        public String getTenGv() { return tenGv; }
        public void setTenGv(String tenGv) { this.tenGv = tenGv; }
        public String getLoaiDiem() { return loaiDiem; }
        public void setLoaiDiem(String loaiDiem) { this.loaiDiem = loaiDiem; }
        public Double getDiemSo() { return diemSo; }
        public void setDiemSo(Double diemSo) { this.diemSo = diemSo; }
        public String getNhanXet() { return nhanXet; }
        public void setNhanXet(String nhanXet) { this.nhanXet = nhanXet; }
    }

    public static class DiemTrungBinhDTO {
        private String maDeTai;
        private Double diemTrungBinh;
        private int soLuongGiangVienCham;
        private String xepLoai; // XUAT_SAC, GIOI, KHA, DAT, KHONG_DAT

        public DiemTrungBinhDTO() {}

        public DiemTrungBinhDTO(String maDeTai, Double diemTrungBinh, int soLuongGiangVienCham, String xepLoai) {
            this.maDeTai = maDeTai;
            this.diemTrungBinh = diemTrungBinh;
            this.soLuongGiangVienCham = soLuongGiangVienCham;
            this.xepLoai = xepLoai;
        }

        public String getMaDeTai() { return maDeTai; }
        public void setMaDeTai(String maDeTai) { this.maDeTai = maDeTai; }
        public Double getDiemTrungBinh() { return diemTrungBinh; }
        public void setDiemTrungBinh(Double diemTrungBinh) { this.diemTrungBinh = diemTrungBinh; }
        public int getSoLuongGiangVienCham() { return soLuongGiangVienCham; }
        public void setSoLuongGiangVienCham(int soLuongGiangVienCham) { this.soLuongGiangVienCham = soLuongGiangVienCham; }
        public String getXepLoai() { return xepLoai; }
        public void setXepLoai(String xepLoai) { this.xepLoai = xepLoai; }
    }
}
