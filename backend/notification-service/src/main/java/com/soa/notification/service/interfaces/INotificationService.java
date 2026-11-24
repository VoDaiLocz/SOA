package com.soa.notification.service.interfaces;

import com.soa.notification.dto.NotificationDTOs.*;
import java.util.List;

public interface INotificationService {
    List<ThongBaoDTO> getThongBaoByNguoiNhan(String maNguoiNhan);
    void danhDauDaDoc(Long id);
    ThongBaoDTO guiThongBao(GuiThongBaoRequest req);
}
