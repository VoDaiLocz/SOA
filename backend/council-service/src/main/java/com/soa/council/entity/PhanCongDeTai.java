package com.soa.council.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "phan_cong_de_tai")
public class PhanCongDeTai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_hoidong", nullable = false, length = 30)
    private String maHoiDong;

    @Column(name = "ma_detai", nullable = false, length = 30)
    private String maDeTai;

    @Column(name = "ten_detai")
    private String tenDeTai;

    @Column(name = "ngay_phan_cong")
    private LocalDateTime ngayPhanCong;

    public PhanCongDeTai() {}

    @PrePersist
    protected void onCreate() {
        if (ngayPhanCong == null) ngayPhanCong = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaHoiDong() { return maHoiDong; }
    public void setMaHoiDong(String maHoiDong) { this.maHoiDong = maHoiDong; }
    public String getMaDeTai() { return maDeTai; }
    public void setMaDeTai(String maDeTai) { this.maDeTai = maDeTai; }
    public String getTenDeTai() { return tenDeTai; }
    public void setTenDeTai(String tenDeTai) { this.tenDeTai = tenDeTai; }
    public LocalDateTime getNgayPhanCong() { return ngayPhanCong; }
    public void setNgayPhanCong(LocalDateTime ngayPhanCong) { this.ngayPhanCong = ngayPhanCong; }
}
