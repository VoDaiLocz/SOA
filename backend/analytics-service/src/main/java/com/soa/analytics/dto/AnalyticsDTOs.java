package com.soa.analytics.dto;

import java.io.Serializable;
import java.util.Map;

public class AnalyticsDTOs {

    public static class ThongKeTrangThaiDTO implements Serializable {
        private int tongSoDeTai;
        private int choDuyet;
        private int daDuyet;
        private int dangThucHien;
        private int choNghiemThu;
        private int daNghiemThu;
        private int daHuy;
        private Map<String, Integer> phanBoTheoTrangThai;

        public ThongKeTrangThaiDTO() {}

        public ThongKeTrangThaiDTO(int tongSoDeTai, int choDuyet, int daDuyet, int dangThucHien,
                                   int choNghiemThu, int daNghiemThu, int daHuy, Map<String, Integer> phanBoTheoTrangThai) {
            this.tongSoDeTai = tongSoDeTai;
            this.choDuyet = choDuyet;
            this.daDuyet = daDuyet;
            this.dangThucHien = dangThucHien;
            this.choNghiemThu = choNghiemThu;
            this.daNghiemThu = daNghiemThu;
            this.daHuy = daHuy;
            this.phanBoTheoTrangThai = phanBoTheoTrangThai;
        }

        public int getTongSoDeTai() { return tongSoDeTai; }
        public void setTongSoDeTai(int tongSoDeTai) { this.tongSoDeTai = tongSoDeTai; }
        public int getChoDuyet() { return choDuyet; }
        public void setChoDuyet(int choDuyet) { this.choDuyet = choDuyet; }
        public int getDaDuyet() { return daDuyet; }
        public void setDaDuyet(int daDuyet) { this.daDuyet = daDuyet; }
        public int getDangThucHien() { return dangThucHien; }
        public void setDangThucHien(int dangThucHien) { this.dangThucHien = dangThucHien; }
        public int getChoNghiemThu() { return choNghiemThu; }
        public void setChoNghiemThu(int choNghiemThu) { this.choNghiemThu = choNghiemThu; }
        public int getDaNghiemThu() { return daNghiemThu; }
        public void setDaNghiemThu(int daNghiemThu) { this.daNghiemThu = daNghiemThu; }
        public int getDaHuy() { return daHuy; }
        public void setDaHuy(int daHuy) { this.daHuy = daHuy; }
        public Map<String, Integer> getPhanBoTheoTrangThai() { return phanBoTheoTrangThai; }
        public void setPhanBoTheoTrangThai(Map<String, Integer> phanBoTheoTrangThai) { this.phanBoTheoTrangThai = phanBoTheoTrangThai; }
    }

    public static class TiLeKetQuaDTO implements Serializable {
        private String kyBaoCao;
        private int tongSo;
        private int soLuongDat;
        private int soLuongKhongDat;
        private double tyLeDatPhanTram;

        public TiLeKetQuaDTO() {}

        public TiLeKetQuaDTO(String kyBaoCao, int tongSo, int soLuongDat, int soLuongKhongDat, double tyLeDatPhanTram) {
            this.kyBaoCao = kyBaoCao;
            this.tongSo = tongSo;
            this.soLuongDat = soLuongDat;
            this.soLuongKhongDat = soLuongKhongDat;
            this.tyLeDatPhanTram = tyLeDatPhanTram;
        }

        public String getKyBaoCao() { return kyBaoCao; }
        public void setKyBaoCao(String kyBaoCao) { this.kyBaoCao = kyBaoCao; }
        public int getTongSo() { return tongSo; }
        public void setTongSo(int tongSo) { this.tongSo = tongSo; }
        public int getSoLuongDat() { return soLuongDat; }
        public void setSoLuongDat(int soLuongDat) { this.soLuongDat = soLuongDat; }
        public int getSoLuongKhongDat() { return soLuongKhongDat; }
        public void setSoLuongKhongDat(int soLuongKhongDat) { this.soLuongKhongDat = soLuongKhongDat; }
        public double getTyLeDatPhanTram() { return tyLeDatPhanTram; }
        public void setTyLeDatPhanTram(double tyLeDatPhanTram) { this.tyLeDatPhanTram = tyLeDatPhanTram; }
    }

    public static class TienDoDeTaiDTO implements Serializable {
        private int dungTienDo;
        private int chamTienDo;
        private int daHoanThanh;

        public TienDoDeTaiDTO() {}

        public TienDoDeTaiDTO(int dungTienDo, int chamTienDo, int daHoanThanh) {
            this.dungTienDo = dungTienDo;
            this.chamTienDo = chamTienDo;
            this.daHoanThanh = daHoanThanh;
        }

        public int getDungTienDo() { return dungTienDo; }
        public void setDungTienDo(int dungTienDo) { this.dungTienDo = dungTienDo; }
        public int getChamTienDo() { return chamTienDo; }
        public void setChamTienDo(int chamTienDo) { this.chamTienDo = chamTienDo; }
        public int getDaHoanThanh() { return daHoanThanh; }
        public void setDaHoanThanh(int daHoanThanh) { this.daHoanThanh = daHoanThanh; }
    }
}
