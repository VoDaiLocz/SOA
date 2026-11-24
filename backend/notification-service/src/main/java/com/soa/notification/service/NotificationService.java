package com.soa.notification.service;

import com.soa.notification.dto.NotificationDTOs.*;
import java.util.List;

public interface NotificationService {
    List<ThongBaoDTO> getThongBaoByNguoiNhan(String maNguoiNhan);
    void danhDauDaDoc(Long id);
    ThongBaoDTO guiThongBao(GuiThongBaoRequest req);
}
