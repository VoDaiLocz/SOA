package com.soa.council.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "thanh_vien_hoi_dong")
public class ThanhVienHoiDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_hoidong", nullable = false, length = 30)
    private String maHoiDong;

    @Column(name = "ma_gv", nullable = false, length = 30)
    private String maGv;

    @Column(name = "ten_gv", length = 100)
    private String tenGv;

    @Column(name = "vai_tro", nullable = false, length = 50)
    private String vaiTro;

    public ThanhVienHoiDong() {}

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
