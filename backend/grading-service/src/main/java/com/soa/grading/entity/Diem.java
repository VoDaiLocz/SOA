package com.soa.grading.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "diem")
public class Diem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_detai", nullable = false, length = 30)
    private String maDeTai;

    @Column(name = "ma_gv", nullable = false, length = 30)
    private String maGv;

    @Column(name = "ten_gv", length = 100)
    private String tenGv;

    @Column(name = "loai_diem", nullable = false, length = 50)
    private String loaiDiem; // DE_CUONG, BAO_VE

    @Column(name = "diem_so", nullable = false)
    private Double diemSo;

    @Column(columnDefinition = "TEXT")
    private String nhanXet;

    @Column(name = "ngay_cham")
    private LocalDateTime ngayCham;

    @PrePersist
    protected void onCreate() {
        if (ngayCham == null) ngayCham = LocalDateTime.now();
    }

    public Diem() {}

    public Diem(String maDeTai, String maGv, String tenGv, String loaiDiem, Double diemSo, String nhanXet) {
        this.maDeTai = maDeTai;
        this.maGv = maGv;
        this.tenGv = tenGv;
        this.loaiDiem = loaiDiem;
        this.diemSo = diemSo;
        this.nhanXet = nhanXet;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaDeTai() { return maDeTai; }
    public void setMaDeTai(String maDeTai) { this.maDeTai = maDeTai; }
    public String getMaGv() { return maGv; }
    public void setMaGv(String maGv) { this.maGv = maGv; }
    public String getTenGv() { return tenGv; }
    public void setTenGv(String tenGv) { this.tenGv = tenGv; }
    public String getLoaiDiem() { return loaiDiem; }
    public void setLoaiDiem(String loaiDiem) { this.loaiDiem = loaiDiem; }
    public Double getDiemSo() { return diemSo; }
    public void setDiemSo(Double diemSo) { this.diemSo = diemSo; }
    public String getNhanXet() { return nhanXet; }
    public void setNhanXet(String nhanXet) { this.nhanXet = nhanXet; }
    public LocalDateTime getNgayCham() { return ngayCham; }
    public void setNgayCham(LocalDateTime ngayCham) { this.ngayCham = ngayCham; }
}
