package com.soa.council.dto;

import java.time.LocalDate;
import java.util.List;

public class CouncilDTOs {

    public static class HoiDongDTO {
        private String maHoiDong;
        private String tenHoiDong;
        private String loaiHoiDong;
        private LocalDate ngayHop;
        private String diaDiem;
        private String trangThai;
        private List<ThanhVienHoiDongDTO> thanhVien;
        private List<PhanCongDeTaiDTO> deTaiPhanCong;

        public HoiDongDTO() {}

        public HoiDongDTO(String maHoiDong, String tenHoiDong, String loaiHoiDong, LocalDate ngayHop, String diaDiem, String trangThai, List<ThanhVienHoiDongDTO> thanhVien, List<PhanCongDeTaiDTO> deTaiPhanCong) {
            this.maHoiDong = maHoiDong;
            this.tenHoiDong = tenHoiDong;
            this.loaiHoiDong = loaiHoiDong;
            this.ngayHop = ngayHop;
            this.diaDiem = diaDiem;
            this.trangThai = trangThai;
            this.thanhVien = thanhVien;
            this.deTaiPhanCong = deTaiPhanCong;
        }

        public String getMaHoiDong() { return maHoiDong; }
        public void setMaHoiDong(String maHoiDong) { this.maHoiDong = maHoiDong; }
        public String getTenHoiDong() { return tenHoiDong; }
        public void setTenHoiDong(String tenHoiDong) { this.tenHoiDong = tenHoiDong; }
        public String getLoaiHoiDong() { return loaiHoiDong; }
        public void setLoaiHoiDong(String loaiHoiDong) { this.loaiHoiDong = loaiHoiDong; }
        public LocalDate getNgayHop() { return ngayHop; }
        public void setNgayHop(LocalDate ngayHop) { this.ngayHop = ngayHop; }
        public String getDiaDiem() { return diaDiem; }
        public void setDiaDiem(String diaDiem) { this.diaDiem = diaDiem; }
        public String getTrangThai() { return trangThai; }
        public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
        public List<ThanhVienHoiDongDTO> getThanhVien() { return thanhVien; }
        public void setThanhVien(List<ThanhVienHoiDongDTO> thanhVien) { this.thanhVien = thanhVien; }
        public List<PhanCongDeTaiDTO> getDeTaiPhanCong() { return deTaiPhanCong; }
        public void setDeTaiPhanCong(List<PhanCongDeTaiDTO> deTaiPhanCong) { this.deTaiPhanCong = deTaiPhanCong; }
    }

    public static class ThanhVienHoiDongDTO {
        private Long id;
        private String maHoiDong;
        private String maGv;
        private String tenGv;
        private String vaiTro;

        public ThanhVienHoiDongDTO() {}

        public ThanhVienHoiDongDTO(Long id, String maHoiDong, String maGv, String tenGv, String vaiTro) {
            this.id = id;
            this.maHoiDong = maHoiDong;
            this.maGv = maGv;
            this.tenGv = tenGv;
            this.vaiTro = vaiTro;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getMaHoiDong() { return maHoiDong; }
        public void setMaHoiDong(String maHoiDong) { this.maHoiDong = maHoiDong; }
        public String getMaGv() { return maGv; }
        public void setMaGv(String maGv) { this.maGv = maGv; }
        public String getTenGv() { return tenGv; }
        public void setTenGv(String tenGv) { this.tenGv = tenGv; }
        public String getVaiTro() { return vaiTro; }
        public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    }

    public static class PhanCongDeTaiDTO {
        private Long id;
        private String maHoiDong;
        private String maDeTai;
        private String tenDeTai;

        public PhanCongDeTaiDTO() {}

        public PhanCongDeTaiDTO(Long id, String maHoiDong, String maDeTai, String tenDeTai) {
            this.id = id;
            this.maHoiDong = maHoiDong;
            this.maDeTai = maDeTai;
            this.tenDeTai = tenDeTai;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getMaHoiDong() { return maHoiDong; }
        public void setMaHoiDong(String maHoiDong) { this.maHoiDong = maHoiDong; }
        public String getMaDeTai() { return maDeTai; }
        public void setMaDeTai(String maDeTai) { this.maDeTai = maDeTai; }
        public String getTenDeTai() { return tenDeTai; }
        public void setTenDeTai(String tenDeTai) { this.tenDeTai = tenDeTai; }
    }

    public static class TaoHoiDongRequest {
        private String maHoiDong;
        private String tenHoiDong;
        private String loaiHoiDong;
        private LocalDate ngayHop;
        private String diaDiem;

        public TaoHoiDongRequest() {}

        public String getMaHoiDong() { return maHoiDong; }
        public void setMaHoiDong(String maHoiDong) { this.maHoiDong = maHoiDong; }
        public String getTenHoiDong() { return tenHoiDong; }
        public void setTenHoiDong(String tenHoiDong) { this.tenHoiDong = tenHoiDong; }
        public String getLoaiHoiDong() { return loaiHoiDong; }
        public void setLoaiHoiDong(String loaiHoiDong) { this.loaiHoiDong = loaiHoiDong; }
        public LocalDate getNgayHop() { return ngayHop; }
        public void setNgayHop(LocalDate ngayHop) { this.ngayHop = ngayHop; }
        public String getDiaDiem() { return diaDiem; }
        public void setDiaDiem(String diaDiem) { this.diaDiem = diaDiem; }
    }

    public static class PhanCongGVRequest {
        private String maGv;
        private String tenGv;
        private String vaiTro;

        public PhanCongGVRequest() {}

        public String getMaGv() { return maGv; }
        public void setMaGv(String maGv) { this.maGv = maGv; }
        public String getTenGv() { return tenGv; }
        public void setTenGv(String tenGv) { this.tenGv = tenGv; }
        public String getVaiTro() { return vaiTro; }
        public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    }

    public static class GanDeTaiRequest {
        private String maDeTai;
        private String tenDeTai;

        public GanDeTaiRequest() {}

        public String getMaDeTai() { return maDeTai; }
        public void setMaDeTai(String maDeTai) { this.maDeTai = maDeTai; }
        public String getTenDeTai() { return tenDeTai; }
        public void setTenDeTai(String tenDeTai) { this.tenDeTai = tenDeTai; }
    }
}
