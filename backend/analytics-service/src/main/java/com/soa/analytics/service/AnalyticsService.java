package com.soa.analytics.service;

import com.soa.analytics.dto.AnalyticsDTOs.*;

public interface AnalyticsService {
    ThongKeTrangThaiDTO getThongKeTheoTrangThai();
    TiLeKetQuaDTO getTiLeKetQua(String khoa);
    TienDoDeTaiDTO getTienDoThucHien();
    byte[] xuatBaoCao(String dinhDang);
}
