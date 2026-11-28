package com.soa.analytics.service;

import com.soa.analytics.client.DeTaiClient;
import com.soa.analytics.dto.AnalyticsDTOs.*;
import com.soa.analytics.dto.ApiResponse;
import com.soa.analytics.repository.SoLieuTongHopRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock private SoLieuTongHopRepository repository;
    @Mock private DeTaiClient deTaiClient;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void getThongKeTheoTrangThaiCountsTopicsCorrectly() {
        DeTaiClientDTO d1 = new DeTaiClientDTO();
        d1.setTrangThai("DA_NGHIEM_THU");
        DeTaiClientDTO d2 = new DeTaiClientDTO();
        d2.setTrangThai("DANG_THUC_HIEN");

        when(deTaiClient.getAllDeTai()).thenReturn(ApiResponse.ok(List.of(d1, d2)));

        ThongKeTrangThaiDTO stats = analyticsService.getThongKeTheoTrangThai();
        assertEquals(2, stats.getTongSoDeTai());
        assertEquals(1, stats.getDaNghiemThu());
        assertEquals(1, stats.getDangThucHien());
    }

    @Test
    void getThongKeTheoTrangThaiFallsBackGracefullyWhenFeignFails() {
        when(deTaiClient.getAllDeTai()).thenThrow(new RuntimeException("Connection refused"));

        ThongKeTrangThaiDTO stats = analyticsService.getThongKeTheoTrangThai();
        assertEquals(0, stats.getTongSoDeTai());
    }
}
