package com.soa.council.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hoi_dong")
public class HoiDong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_hoidong", unique = true, nullable = false, length = 30)
    private String maHoiDong;

    @Column(name = "ten_hoi_dong", nullable = false)
    private String tenHoiDong;

    @Column(name = "loai_hoi_dong", nullable = false, length = 50)
    private String loaiHoiDong;

    @Column(name = "ngay_hop")
    private LocalDate ngayHop;

    @Column(name = "dia_diem", length = 100)
    private String diaDiem;

    @Column(name = "trang_thai", length = 30)
    private String trangThai = "DANG_HOAT_DONG";

    public HoiDong() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaHoiDong() { return maHoiDong; }
    public void setMaHoiDong(String maHoiDong) { this.maHoiDong = maHoiDong; }
    public String getTenHoiDong() { return tenHoiDong; }
    public void setTenHoiDong(String tenHoiDong) { this.tenHoiDong = tenHoiDong; }
    public String getLoaiHoiDong() { return loaiHoiDong; }
    public void setLoaiHoiDong(String loaiHoiDong) { this.loaiHoiDong = loaiHoiDong; }
    public LocalDate getNgayHop() { return ngayHop; }
    public void setNgayHop(LocalDate ngayHop) { this.ngayHop = ngayHop; }
    public String getDiaDiem() { return diaDiem; }
    public void setDiaDiem(String diaDiem) { this.diaDiem = diaDiem; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
