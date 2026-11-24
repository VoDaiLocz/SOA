package com.soa.detai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dangky", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ma_sv", "ma_detai"})
})
public class DangKy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_detai", nullable = false, length = 30)
    private String maDeTai;

    @Column(name = "ma_sv", nullable = false, length = 30)
    private String maSv;

    @Column(name = "vai_tro_thanh_vien", length = 50)
    private String vaiTroThanhVien = "THANH_VIEN";

    @Column(name = "trang_thai", length = 30)
    private String trangThai = "XAC_NHAN";

    @Column(name = "ngay_dang_ky")
    private LocalDateTime ngayDangKy;

    public DangKy() {}

    @PrePersist
    protected void onCreate() {
        if (ngayDangKy == null) ngayDangKy = LocalDateTime.now();
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
