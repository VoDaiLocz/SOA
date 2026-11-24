package com.soa.analytics.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "so_lieu_tong_hop")
public class SoLieuTongHop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ky_bao_cao", nullable = false, length = 50)
    private String kyBaoCao;

    @Column(name = "tong_so_de_tai")
    private Integer tongSoDeTai;

    @Column(name = "so_de_tai_dat")
    private Integer soDeTaiDat;

    @Column(name = "so_de_tai_khong_dat")
    private Integer soDeTaiKhongDat;

    @Column(name = "so_de_tai_dang_thuc_hien")
    private Integer soDeTaiDangThucHien;

    @Column(name = "ngay_tong_hop")
    private LocalDateTime ngayTongHop;

    @PrePersist
    protected void onCreate() {
        if (ngayTongHop == null) ngayTongHop = LocalDateTime.now();
    }

    public SoLieuTongHop() {}

    public SoLieuTongHop(String kyBaoCao, Integer tongSoDeTai, Integer soDeTaiDat, Integer soDeTaiKhongDat, Integer soDeTaiDangThucHien) {
        this.kyBaoCao = kyBaoCao;
        this.tongSoDeTai = tongSoDeTai;
        this.soDeTaiDat = soDeTaiDat;
        this.soDeTaiKhongDat = soDeTaiKhongDat;
        this.soDeTaiDangThucHien = soDeTaiDangThucHien;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getKyBaoCao() { return kyBaoCao; }
    public void setKyBaoCao(String kyBaoCao) { this.kyBaoCao = kyBaoCao; }
    public Integer getTongSoDeTai() { return tongSoDeTai; }
    public void setTongSoDeTai(Integer tongSoDeTai) { this.tongSoDeTai = tongSoDeTai; }
    public Integer getSoDeTaiDat() { return soDeTaiDat; }
    public void setSoDeTaiDat(Integer soDeTaiDat) { this.soDeTaiDat = soDeTaiDat; }
    public Integer getSoDeTaiKhongDat() { return soDeTaiKhongDat; }
    public void setSoDeTaiKhongDat(Integer soDeTaiKhongDat) { this.soDeTaiKhongDat = soDeTaiKhongDat; }
    public Integer getSoDeTaiDangThucHien() { return soDeTaiDangThucHien; }
    public void setSoDeTaiDangThucHien(Integer soDeTaiDangThucHien) { this.soDeTaiDangThucHien = soDeTaiDangThucHien; }
    public LocalDateTime getNgayTongHop() { return ngayTongHop; }
    public void setNgayTongHop(LocalDateTime ngayTongHop) { this.ngayTongHop = ngayTongHop; }
}
