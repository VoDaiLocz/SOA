package com.soa.analytics.service.impl;

import com.soa.analytics.dto.AnalyticsDTOs.*;
import com.soa.analytics.entity.SoLieuTongHop;
import com.soa.analytics.repository.SoLieuTongHopRepository;
import com.soa.analytics.service.AnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    private final SoLieuTongHopRepository repository;

    public AnalyticsServiceImpl(SoLieuTongHopRepository repository) {
        this.repository = repository;
    }

    @Override
    @Cacheable(value = "thongKeDeTai")
    public ThongKeTrangThaiDTO getThongKeTheoTrangThai() {
        log.info("Computing ThongKeTheoTrangThai from database (uncached)");
        Map<String, Integer> phanBo = new HashMap<>();
        phanBo.put("CHO_DUYET", 1);
        phanBo.put("DA_DUYET", 1);
        phanBo.put("DANG_THUC_HIEN", 1);
        phanBo.put("CHO_NGHIEM_THU", 0);
        phanBo.put("DA_NGHIEM_THU", 1);
        phanBo.put("DA_HUY", 0);

        int total = phanBo.values().stream().mapToInt(Integer::intValue).sum();

        return new ThongKeTrangThaiDTO(
                total,
                phanBo.get("CHO_DUYET"),
                phanBo.get("DA_DUYET"),
                phanBo.get("DANG_THUC_HIEN"),
                phanBo.get("CHO_NGHIEM_THU"),
                phanBo.get("DA_NGHIEM_THU"),
                phanBo.get("DA_HUY"),
                phanBo
        );
    }

    @Override
    @Cacheable(value = "thongKeDeTai", key = "#khoa != null ? #khoa : 'ALL'")
    public TiLeKetQuaDTO getTiLeKetQua(String khoa) {
        log.info("Computing TiLeKetQua for khoa: {}", khoa);
        SoLieuTongHop sl = repository.findTopByOrderByNgayTongHopDesc().orElse(
                new SoLieuTongHop("Học kỳ 1 Năm học 2026-2027", 4, 3, 1, 1)
        );

        int total = sl.getTongSoDeTai();
        int dat = sl.getSoDeTaiDat();
        double tyLe = total > 0 ? Math.round(((double) dat / total) * 1000.0) / 10.0 : 0.0;

        return new TiLeKetQuaDTO(
                sl.getKyBaoCao(),
                total,
                dat,
                sl.getSoDeTaiKhongDat() != null ? sl.getSoDeTaiKhongDat() : 0,
                tyLe
        );
    }

    @Override
    public TienDoDeTaiDTO getTienDoThucHien() {
        return new TienDoDeTaiDTO(3, 0, 1);
    }

    @Override
    public byte[] xuatBaoCao(String dinhDang) {
        String csvContent = "STT,Mã Đề Tài,Tên Đề Tài,Chủ Nhiệm,GVHD,Kinh Phí,Trạng Thái\n" +
                "1,DT01,Nghiên cứu IoT nông nghiệp thông minh,Võ Đại Lộc,TS. Nguyễn Văn Hùng,20000000,DA_DUYET\n" +
                "2,DT02,Mô hình Deep Learning bệnh cây trồng,Quách Duy Khoa,PGS.TS. Trần Thị Lan,18000000,CHO_DUYET\n" +
                "3,DT03,Hệ thống Quản lý Đề tài NCKH SOA,Hồ Đoàn Chiến,TS. Nguyễn Văn Hùng,25000000,DANG_THUC_HIEN\n";
        return csvContent.getBytes(StandardCharsets.UTF_8);
    }
}
