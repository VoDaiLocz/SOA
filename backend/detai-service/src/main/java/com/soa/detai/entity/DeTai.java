package com.soa.detai.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "detai")
public class DeTai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_detai", unique = true, nullable = false, length = 30)
    private String maDeTai;

    @Column(name = "ten_de_tai", nullable = false)
    private String tenDeTai;

    @Column(columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "linh_vuc", length = 100)
    private String linhVuc;

    @Column(name = "kinh_phi_du_kien")
    private Double kinhPhiDuKien = 15000000.0;

    @Column(name = "ma_sv_chu_nhiem", length = 30)
    private String maSvChuNhiem;

    @Column(name = "ma_gvhd", length = 30)
    private String maGvhd;

    @Column(name = "ten_gvhd", length = 100)
    private String tenGvhd;

    @Column(name = "so_luong_hien_tai")
    private Integer soLuongHienTai = 1;

    @Column(name = "so_luong_toi_da")
    private Integer soLuongToiDa = 3;

    @Column(length = 50)
    private String trangThai = "CHO_DUYET";

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    public DeTai() {}

    @PrePersist
    protected void onCreate() {
        if (ngayTao == null) ngayTao = LocalDateTime.now();
        if (ngayCapNhat == null) ngayCapNhat = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        ngayCapNhat = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public LocalDateTime getNgayCapNhat() { return ngayCapNhat; }
    public void setNgayCapNhat(LocalDateTime ngayCapNhat) { this.ngayCapNhat = ngayCapNhat; }
}
