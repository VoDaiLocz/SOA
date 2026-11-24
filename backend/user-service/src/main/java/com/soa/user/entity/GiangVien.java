package com.soa.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "giangvien")
public class GiangVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_gv", unique = true, nullable = false, length = 30)
    private String maGv;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(length = 100)
    private String email;

    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(name = "hoc_vi", length = 50)
    private String hocVi;

    @Column(length = 100)
    private String khoa;

    @Column(name = "bo_mon", length = 100)
    private String boMon;

    @Column(name = "huong_nghien_cuu", columnDefinition = "TEXT")
    private String huongNghienCuu;

    public GiangVien() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaGv() { return maGv; }
    public void setMaGv(String maGv) { this.maGv = maGv; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getHocVi() { return hocVi; }
    public void setHocVi(String hocVi) { this.hocVi = hocVi; }
    public String getKhoa() { return khoa; }
    public void setKhoa(String khoa) { this.khoa = khoa; }
    public String getBoMon() { return boMon; }
    public void setBoMon(String boMon) { this.boMon = boMon; }
    public String getHuongNghienCuu() { return huongNghienCuu; }
    public void setHuongNghienCuu(String huongNghienCuu) { this.huongNghienCuu = huongNghienCuu; }
}
