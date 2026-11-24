package com.soa.detai.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DeTaiDTOs {

    public static class DeTaiDTO implements Serializable {
        private String maDeTai;
        private String tenDeTai;
        private String moTa;
        private String linhVuc;
        private Double kinhPhiDuKien;
        private String maSvChuNhiem;
        private String maGvhd;
        private String tenGvhd;
        private Integer soLuongHienTai;
        private Integer soLuongToiDa;
        private String trangThai;
        private LocalDateTime ngayTao;

        public DeTaiDTO() {}

        public DeTaiDTO(String maDeTai, String tenDeTai, String moTa, String linhVuc, Double kinhPhiDuKien, String maSvChuNhiem, String maGvhd, String tenGvhd, Integer soLuongHienTai, Integer soLuongToiDa, String trangThai, LocalDateTime ngayTao) {
            this.maDeTai = maDeTai;
            this.tenDeTai = tenDeTai;
            this.moTa = moTa;
            this.linhVuc = linhVuc;
            this.kinhPhiDuKien = kinhPhiDuKien;
            this.maSvChuNhiem = maSvChuNhiem;
            this.maGvhd = maGvhd;
            this.tenGvhd = tenGvhd;
            this.soLuongHienTai = soLuongHienTai;
            this.soLuongToiDa = soLuongToiDa;
            this.trangThai = trangThai;
            this.ngayTao = ngayTao;
        }

        public String getMaDeTai() { return maDeTai; }
        public void setMaDeTai(String maDeTai) { this.maDeTai = maDeTai; }
        public String getTenDeTai() { return tenDeTai; }
        public void setTenDeTai(String tenDeTai) { this.tenDeTai = tenDeTai; }
        public String getMoTa() { return moTa; }
        public void setMoTa(String moTa) { this.moTa = moTa; }
        public String getLinhVuc() { return linhVuc; }
        public void setLinhVuc(String linhVuc) { this.linhVuc = linhVuc; }
        public Double getKinhPhiDuKien() { return kinhPhiDuKien; }
        public void setKinhPhiDuKien(Double kinhPhiDuKien) { this.kinhPhiDuKien = kinhPhiDuKien; }
        public String getMaSvChuNhiem() { return maSvChuNhiem; }
        public void setMaSvChuNhiem(String maSvChuNhiem) { this.maSvChuNhiem = maSvChuNhiem; }
        public String getMaGvhd() { return maGvhd; }
        public void setMaGvhd(String maGvhd) { this.maGvhd = maGvhd; }
        public String getTenGvhd() { return tenGvhd; }
        public void setTenGvhd(String tenGvhd) { this.tenGvhd = tenGvhd; }
        public Integer getSoLuongHienTai() { return soLuongHienTai; }
        public void setSoLuongHienTai(Integer soLuongHienTai) { this.soLuongHienTai = soLuongHienTai; }
        public Integer getSoLuongToiDa() { return soLuongToiDa; }
        public void setSoLuongToiDa(Integer soLuongToiDa) { this.soLuongToiDa = soLuongToiDa; }
        public String getTrangThai() { return trangThai; }
        public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
        public LocalDateTime getNgayTao() { return ngayTao; }
        public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }
    }

    public static class TaoDeTaiRequest {
        private String maDeTai;
        private String tenDeTai;
        private String moTa;
        private String linhVuc;
        private Double kinhPhiDuKien;
        private String maSvChuNhiem;
        private String maGvhd;
        private String tenGvhd;
        private Integer soLuongToiDa;

        public TaoDeTaiRequest() {}

        public String getMaDeTai() { return maDeTai; }
        public void setMaDeTai(String maDeTai) { this.maDeTai = maDeTai; }
        public String getTenDeTai() { return tenDeTai; }
        public void setTenDeTai(String tenDeTai) { this.tenDeTai = tenDeTai; }
        public String getMoTa() { return moTa; }
        public void setMoTa(String moTa) { this.moTa = moTa; }
        public String getLinhVuc() { return linhVuc; }
        public void setLinhVuc(String linhVuc) { this.linhVuc = linhVuc; }
        public Double getKinhPhiDuKien() { return kinhPhiDuKien; }
        public void setKinhPhiDuKien(Double kinhPhiDuKien) { this.kinhPhiDuKien = kinhPhiDuKien; }
        public String getMaSvChuNhiem() { return maSvChuNhiem; }
        public void setMaSvChuNhiem(String maSvChuNhiem) { this.maSvChuNhiem = maSvChuNhiem; }
        public String getMaGvhd() { return maGvhd; }
        public void setMaGvhd(String maGvhd) { this.maGvhd = maGvhd; }
        public String getTenGvhd() { return tenGvhd; }
        public void setTenGvhd(String tenGvhd) { this.tenGvhd = tenGvhd; }
        public Integer getSoLuongToiDa() { return soLuongToiDa; }
        public void setSoLuongToiDa(Integer soLuongToiDa) { this.soLuongToiDa = soLuongToiDa; }
    }

    public static class DangKyDTO {
        private Long id;
        private String maDeTai;
        private String maSv;
        private String vaiTroThanhVien;
        private String trangThai;
        private LocalDateTime ngayDangKy;

        public DangKyDTO() {}

        public DangKyDTO(Long id, String maDeTai, String maSv, String vaiTroThanhVien, String trangThai, LocalDateTime ngayDangKy) {
            this.id = id;
            this.maDeTai = maDeTai;
            this.maSv = maSv;
            this.vaiTroThanhVien = vaiTroThanhVien;
            this.trangThai = trangThai;
            this.ngayDangKy = ngayDangKy;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getMaDeTai() { return maDeTai; }
        public void setMaDeTai(String maDeTai) { this.maDeTai = maDeTai; }
        public String getMaSv() { return maSv; }
        public void setMaSv(String maSv) { this.maSv = maSv; }
        public String getVaiTroThanhVien() { return vaiTroThanhVien; }
        public void setVaiTroThanhVien(String vaiTroThanhVien) { this.vaiTroThanhVien = vaiTroThanhVien; }
        public String getTrangThai() { return trangThai; }
        public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
        public LocalDateTime getNgayDangKy() { return ngayDangKy; }
        public void setNgayDangKy(LocalDateTime ngayDangKy) { this.ngayDangKy = ngayDangKy; }
    }

    public static class DangKyRequest {
        private String maDeTai;
        private String maSv;

        public DangKyRequest() {}
        public DangKyRequest(String maDeTai, String maSv) {
            this.maDeTai = maDeTai;
            this.maSv = maSv;
        }

        public String getMaDeTai() { return maDeTai; }
        public void setMaDeTai(String maDeTai) { this.maDeTai = maDeTai; }
        public String getMaSv() { return maSv; }
        public void setMaSv(String maSv) { this.maSv = maSv; }
    }

    public static class SinhVienClientDTO {
        private String maSv;
        private String hoTen;
        private String trangThai;

        public SinhVienClientDTO() {}

        public String getMaSv() { return maSv; }
        public void setMaSv(String maSv) { this.maSv = maSv; }
        public String getHoTen() { return hoTen; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }
        public String getTrangThai() { return trangThai; }
        public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    }
}
