package com.soa.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lop")
public class Lop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_lop", unique = true, nullable = false, length = 30)
    private String maLop;

    @Column(name = "ten_lop", nullable = false, length = 100)
    private String tenLop;

    @Column(name = "ma_khoa", length = 30)
    private String maKhoa;

    public Lop() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }
    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }
    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }
}
