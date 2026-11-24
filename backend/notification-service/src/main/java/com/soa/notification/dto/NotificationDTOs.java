package com.soa.notification.dto;

import java.time.LocalDateTime;

public class NotificationDTOs {

    public static class ThongBaoDTO {
        private Long id;
        private String maNguoiNhan;
        private String tieuDe;
        private String noiDung;
        private String loaiThongBao;
        private Boolean daDoc;
        private LocalDateTime ngayGui;

        public ThongBaoDTO() {}

        public ThongBaoDTO(Long id, String maNguoiNhan, String tieuDe, String noiDung, String loaiThongBao, Boolean daDoc, LocalDateTime ngayGui) {
            this.id = id;
            this.maNguoiNhan = maNguoiNhan;
            this.tieuDe = tieuDe;
            this.noiDung = noiDung;
            this.loaiThongBao = loaiThongBao;
            this.daDoc = daDoc;
            this.ngayGui = ngayGui;
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

    public static class GuiThongBaoRequest {
        private String maNguoiNhan;
        private String tieuDe;
        private String noiDung;
        private String loaiThongBao;

        public GuiThongBaoRequest() {}

        public GuiThongBaoRequest(String maNguoiNhan, String tieuDe, String noiDung, String loaiThongBao) {
            this.maNguoiNhan = maNguoiNhan;
            this.tieuDe = tieuDe;
            this.noiDung = noiDung;
            this.loaiThongBao = loaiThongBao;
        }

        public String getMaNguoiNhan() { return maNguoiNhan; }
        public void setMaNguoiNhan(String maNguoiNhan) { this.maNguoiNhan = maNguoiNhan; }
        public String getTieuDe() { return tieuDe; }
        public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
        public String getNoiDung() { return noiDung; }
        public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
        public String getLoaiThongBao() { return loaiThongBao; }
        public void setLoaiThongBao(String loaiThongBao) { this.loaiThongBao = loaiThongBao; }
    }
}
