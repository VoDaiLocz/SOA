package com.soa.notification.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "thong_bao")
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_nguoi_nhan", nullable = false, length = 50)
    private String maNguoiNhan;

    @Column(name = "tieu_de", nullable = false)
    private String tieuDe;

    @Column(name = "noi_dung", nullable = false, columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "loai_thong_bao", length = 50)
    private String loaiThongBao = "HE_THONG";

    @Column(name = "da_doc")
    private Boolean daDoc = false;

    @Column(name = "ngay_gui")
    private LocalDateTime ngayGui;

    @PrePersist
    protected void onCreate() {
        if (ngayGui == null) ngayGui = LocalDateTime.now();
        if (daDoc == null) daDoc = false;
        if (loaiThongBao == null) loaiThongBao = "HE_THONG";
    }

    public ThongBao() {}

    public ThongBao(String maNguoiNhan, String tieuDe, String noiDung, String loaiThongBao) {
        this.maNguoiNhan = maNguoiNhan;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.loaiThongBao = loaiThongBao != null ? loaiThongBao : "HE_THONG";
        this.daDoc = false;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaNguoiNhan() { return maNguoiNhan; }
    public void setMaNguoiNhan(String maNguoiNhan) { this.maNguoiNhan = maNguoiNhan; }
    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
    public String getLoaiThongBao() { return loaiThongBao; }
    public void setLoaiThongBao(String loaiThongBao) { this.loaiThongBao = loaiThongBao; }
    public Boolean getDaDoc() { return daDoc; }
    public void setDaDoc(Boolean daDoc) { this.daDoc = daDoc; }
    public LocalDateTime getNgayGui() { return ngayGui; }
    public void setNgayGui(LocalDateTime ngayGui) { this.ngayGui = ngayGui; }
}
