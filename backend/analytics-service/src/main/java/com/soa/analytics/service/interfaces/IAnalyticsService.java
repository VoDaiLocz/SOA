package com.soa.analytics.service.interfaces;

import com.soa.analytics.dto.AnalyticsDTOs.*;

public interface IAnalyticsService {
    ThongKeTrangThaiDTO getThongKeTheoTrangThai();
    TiLeKetQuaDTO getTiLeKetQua(String khoa);
    TienDoDeTaiDTO getTienDoThucHien();
    byte[] xuatBaoCao(String dinhDang);
}
