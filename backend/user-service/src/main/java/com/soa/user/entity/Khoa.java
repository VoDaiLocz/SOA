package com.soa.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "khoa")
public class Khoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_khoa", unique = true, nullable = false, length = 30)
    private String maKhoa;

    @Column(name = "ten_khoa", nullable = false, length = 100)
    private String tenKhoa;

    public Khoa() {}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaKhoa() { return maKhoa; }
    public void setMaKhoa(String maKhoa) { this.maKhoa = maKhoa; }
    public String getTenKhoa() { return tenKhoa; }
    public void setTenKhoa(String tenKhoa) { this.tenKhoa = tenKhoa; }
}
