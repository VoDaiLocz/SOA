package com.soa.user.dto;

public class UserDTOs {

    public static class SinhVienDTO {
        private String maSv;
        private String hoTen;
        private String email;
        private String soDienThoai;
        private String chuyenNganh;
        private String khoa;
        private String lop;
        private Double gpa;
        private String trangThai;

        public SinhVienDTO() {}

        public SinhVienDTO(String maSv, String hoTen, String email, String soDienThoai, String chuyenNganh, String khoa, String lop, Double gpa, String trangThai) {
            this.maSv = maSv;
            this.hoTen = hoTen;
            this.email = email;
            this.soDienThoai = soDienThoai;
            this.chuyenNganh = chuyenNganh;
            this.khoa = khoa;
            this.lop = lop;
            this.gpa = gpa;
            this.trangThai = trangThai;
        }

        public String getMaSv() { return maSv; }
        public void setMaSv(String maSv) { this.maSv = maSv; }
        public String getHoTen() { return hoTen; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getSoDienThoai() { return soDienThoai; }
        public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
        public String getChuyenNganh() { return chuyenNganh; }
        public void setChuyenNganh(String chuyenNganh) { this.chuyenNganh = chuyenNganh; }
        public String getKhoa() { return khoa; }
        public void setKhoa(String khoa) { this.khoa = khoa; }
        public String getLop() { return lop; }
        public void setLop(String lop) { this.lop = lop; }
        public Double getGpa() { return gpa; }
        public void setGpa(Double gpa) { this.gpa = gpa; }
        public String getTrangThai() { return trangThai; }
        public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    }

    public static class GiangVienDTO {
        private String maGv;
        private String hoTen;
        private String email;
        private String soDienThoai;
        private String hocVi;
        private String khoa;
        private String boMon;
        private String huongNghienCuu;

        public GiangVienDTO() {}

        public GiangVienDTO(String maGv, String hoTen, String email, String soDienThoai, String hocVi, String khoa, String boMon, String huongNghienCuu) {
            this.maGv = maGv;
            this.hoTen = hoTen;
            this.email = email;
            this.soDienThoai = soDienThoai;
            this.hocVi = hocVi;
            this.khoa = khoa;
            this.boMon = boMon;
            this.huongNghienCuu = huongNghienCuu;
        }

        public String getMaGv() { return maGv; }
        public void setMaGv(String maGv) { this.maGv = maGv; }
        public String getHoTen() { return hoTen; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getSoDienThoai() { return soDienThoai; }
        public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
        public String getHocVi() { return hocVi; }
        public void setHocVi(String hocVi) { this.hocVi = hocVi; }
        public String getKhoa() { return khoa; }
        public void setKhoa(String khoa) { this.khoa = khoa; }
        public String getBoMon() { return boMon; }
        public void setBoMon(String boMon) { this.boMon = boMon; }
        public String getHuongNghienCuu() { return huongNghienCuu; }
        public void setHuongNghienCuu(String huongNghienCuu) { this.huongNghienCuu = huongNghienCuu; }
    }

    public static class KhoaDTO {
        private String maKhoa;
        private String tenKhoa;

        public KhoaDTO() {}
        public KhoaDTO(String maKhoa, String tenKhoa) {
            this.maKhoa = maKhoa;
            this.tenKhoa = tenKhoa;
        }

        public String getMaKhoa() { return maKhoa; }
        public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }
        public String getTenKhoa() { return tenKhoa; }
        public void setTenKhoa(String tenKhoa) { this.tenKhoa = tenKhoa; }
    }

    public static class LopDTO {
        private String maLop;
        private String tenLop;
        private String maKhoa;

        public LopDTO() {}
        public LopDTO(String maLop, String tenLop, String maKhoa) {
            this.maLop = maLop;
            this.tenLop = tenLop;
            this.maKhoa = maKhoa;
        }

        public String getMaLop() { return maLop; }
        public void setMaLop(String maLop) { this.maLop = maLop; }
        public String getTenLop() { return tenLop; }
        public void setTenLop(String tenLop) { this.tenLop = tenLop; }
        public String getMaKhoa() { return maKhoa; }
        public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }
    }
}
