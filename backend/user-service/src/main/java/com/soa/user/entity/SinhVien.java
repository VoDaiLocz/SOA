package com.soa.user.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sinhvien")
public class SinhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_sv", unique = true, nullable = false, length = 30)
    private String maSv;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(length = 100)
    private String email;

    @Column(name = "so_dien_thoai", length = 20)
    private String soDienThoai;

    @Column(name = "chuyen_nganh", length = 100)
    private String chuyenNganh;

    @Column(length = 100)
    private String khoa;

    @Column(length = 50)
    private String lop;

    private Double gpa;

    @Column(name = "trang_thai", length = 30)
    private String trangThai = "DANG_HOC";

    public SinhVien() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaSv() { return maSv; }
    public void setMaSv(String maSv) { this.maSv = maSv; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public String getChuyenNganh() { return chuyenNganh; }
    public void setChuyenNganh(String chuyenNganh) { this.chuyenNganh = chuyenNganh; }
    public String getKhoa() { return khoa; }
    public void setKhoa(String khoa) { this.khoa = khoa; }
    public String getLop() { return lop; }
    public void setLop(String lop) { this.lop = lop; }
    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
