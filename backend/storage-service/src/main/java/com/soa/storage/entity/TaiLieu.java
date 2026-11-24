package com.soa.storage.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tai_lieu")
public class TaiLieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_detai", nullable = false, length = 30)
    private String maDeTai;

    @Column(name = "ten_file", nullable = false)
    private String tenFile;

    @Column(name = "duong_dan", nullable = false, length = 500)
    private String duongDan;

    @Column(name = "loai_tai_lieu", length = 50)
    private String loaiTaiLieu; // THUYET_MINH, BAO_CAO_TIEN_DO, BAO_CAO_NGHIEM_THU

    @Column(name = "kich_thuoc")
    private Long kichThuoc;

    @Column(name = "nguoi_upload", length = 50)
    private String nguoiUpload;

    @Column(name = "ngay_upload")
    private LocalDateTime ngayUpload;

    @PrePersist
    protected void onCreate() {
        if (ngayUpload == null) ngayUpload = LocalDateTime.now();
    }

    public TaiLieu() {}

    public TaiLieu(String maDeTai, String tenFile, String duongDan, String loaiTaiLieu, Long kichThuoc, String nguoiUpload) {
        this.maDeTai = maDeTai;
        this.tenFile = tenFile;
        this.duongDan = duongDan;
        this.loaiTaiLieu = loaiTaiLieu != null ? loaiTaiLieu : "THUYET_MINH";
        this.kichThuoc = kichThuoc;
        this.nguoiUpload = nguoiUpload;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaDeTai() { return maDeTai; }
    public void setMaDeTai(String maDeTai) { this.maDeTai = maDeTai; }
    public String getTenFile() { return tenFile; }
    public void setTenFile(String tenFile) { this.tenFile = tenFile; }
    public String getDuongDan() { return duongDan; }
    public void setDuongDan(String duongDan) { this.duongDan = duongDan; }
    public String getLoaiTaiLieu() { return loaiTaiLieu; }
    public void setLoaiTaiLieu(String loaiTaiLieu) { this.loaiTaiLieu = loaiTaiLieu; }
    public Long getKichThuoc() { return kichThuoc; }
    public void setKichThuoc(Long kichThuoc) { this.kichThuoc = kichThuoc; }
    public String getNguoiUpload() { return nguoiUpload; }
    public void setNguoiUpload(String nguoiUpload) { this.nguoiUpload = nguoiUpload; }
    public LocalDateTime getNgayUpload() { return ngayUpload; }
    public void setNgayUpload(LocalDateTime ngayUpload) { this.ngayUpload = ngayUpload; }
}
