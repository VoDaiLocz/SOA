package com.soa.analytics.controller;

import com.soa.analytics.dto.ApiResponse;
import com.soa.analytics.dto.AnalyticsDTOs.*;
import com.soa.analytics.service.interfaces.IAnalyticsService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/thong-ke")
public class AnalyticsController {

    private final IAnalyticsService analyticsService;

    public AnalyticsController(IAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/detai")
    public ResponseEntity<ApiResponse<ThongKeTrangThaiDTO>> getThongKeTheoTrangThai() {
        return ResponseEntity.ok(ApiResponse.ok(analyticsService.getThongKeTheoTrangThai()));
    }

    @GetMapping("/ket-qua")
    public ResponseEntity<ApiResponse<TiLeKetQuaDTO>> getTiLeKetQua(@RequestParam(required = false) String khoa) {
        return ResponseEntity.ok(ApiResponse.ok(analyticsService.getTiLeKetQua(khoa)));
    }

    @GetMapping("/ti-do")
    public ResponseEntity<ApiResponse<TienDoDeTaiDTO>> getTienDoThucHien() {
        return ResponseEntity.ok(ApiResponse.ok(analyticsService.getTienDoThucHien()));
    }

    @GetMapping("/xuat")
    public ResponseEntity<byte[]> xuatBaoCao(@RequestParam(defaultValue = "excel") String dinhDang) {
        byte[] data = analyticsService.xuatBaoCao(dinhDang);
        String filename = "Bao_Cao_Thong_Ke_NCKH." + (dinhDang.equalsIgnoreCase("excel") ? "csv" : "txt");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(data);
    }
}
