package com.soa.analytics.service;

import com.soa.analytics.client.DeTaiClient;
import com.soa.analytics.dto.AnalyticsDTOs.*;
import com.soa.analytics.dto.ApiResponse;
import com.soa.analytics.entity.SoLieuTongHop;
import com.soa.analytics.repository.SoLieuTongHopRepository;
import com.soa.analytics.service.interfaces.IAnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService implements IAnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);

    private final SoLieuTongHopRepository repository;
    private final DeTaiClient deTaiClient;

    public AnalyticsService(SoLieuTongHopRepository repository, DeTaiClient deTaiClient) {
        this.repository = repository;
        this.deTaiClient = deTaiClient;
    }

    private List<DeTaiClientDTO> fetchAllDeTai() {
        try {
            ApiResponse<List<DeTaiClientDTO>> response = deTaiClient.getAllDeTai();
            if (response == null || response.getData() == null) {
                return Collections.emptyList();
            }
            return response.getData();
        } catch (Exception e) {
            log.warn("Không thể kết nối detai-service để lấy dữ liệu thống kê: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    @Cacheable(value = "thongKeTrangThai")
    public ThongKeTrangThaiDTO getThongKeTheoTrangThai() {
        log.info("Computing ThongKeTheoTrangThai from detai-service");
        List<DeTaiClientDTO> list = fetchAllDeTai();

        Map<String, Integer> phanBo = new LinkedHashMap<>();
        phanBo.put("CHO_DUYET", 0);
        phanBo.put("DA_DUYET", 0);
        phanBo.put("DANG_THUC_HIEN", 0);
        phanBo.put("CHO_NGHIEM_THU", 0);
        phanBo.put("DA_NGHIEM_THU", 0);
        phanBo.put("DA_HUY", 0);

        for (DeTaiClientDTO dt : list) {
            String status = dt.getTrangThai() != null ? dt.getTrangThai() : "CHO_DUYET";
            phanBo.put(status, phanBo.getOrDefault(status, 0) + 1);
        }

        return new ThongKeTrangThaiDTO(
                list.size(),
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
    @Cacheable(value = "tiLeKetQua", key = "#khoa != null ? #khoa : 'ALL'")
    public TiLeKetQuaDTO getTiLeKetQua(String khoa) {
        log.info("Computing TiLeKetQua for khoa: {}", khoa);
        List<DeTaiClientDTO> list = fetchAllDeTai();
        if (!list.isEmpty()) {
            int total = list.size();
            int dat = (int) list.stream().filter(d -> "DA_NGHIEM_THU".equals(d.getTrangThai())).count();
            int khongDat = (int) list.stream().filter(d -> "DA_HUY".equals(d.getTrangThai())).count();
            double tyLe = Math.round(((double) dat / total) * 1000.0) / 10.0;
            String label = (khoa != null && !khoa.isBlank()) ? "Khoa " + khoa : "Hiện tại";
            return new TiLeKetQuaDTO(label, total, dat, khongDat, tyLe);
        }

        return repository.findTopByOrderByNgayTongHopDesc()
                .map(sl -> {
                    int total = sl.getTongSoDeTai() != null ? sl.getTongSoDeTai() : 0;
                    int dat = sl.getSoDeTaiDat() != null ? sl.getSoDeTaiDat() : 0;
                    int khongDat = sl.getSoDeTaiKhongDat() != null ? sl.getSoDeTaiKhongDat() : 0;
                    double tyLe = total > 0 ? Math.round(((double) dat / total) * 1000.0) / 10.0 : 0.0;
                    return new TiLeKetQuaDTO(sl.getKyBaoCao(), total, dat, khongDat, tyLe);
                })
                .orElseGet(() -> new TiLeKetQuaDTO("Hiện tại", 0, 0, 0, 0.0));
    }

    @Override
    public TienDoDeTaiDTO getTienDoThucHien() {
        List<DeTaiClientDTO> list = fetchAllDeTai();
        int dungTienDo = 0;
        int chamTienDo = 0;
        int daHoanThanh = 0;

        for (DeTaiClientDTO dt : list) {
            String status = dt.getTrangThai();
            if ("DA_NGHIEM_THU".equals(status)) {
                daHoanThanh++;
            } else if ("DA_HUY".equals(status)) {
                chamTienDo++;
            } else {
                dungTienDo++;
            }
        }

        return new TienDoDeTaiDTO(dungTienDo, chamTienDo, daHoanThanh);
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    @Override
    public byte[] xuatBaoCao(String dinhDang) {
        List<DeTaiClientDTO> list = fetchAllDeTai();
        StringBuilder sb = new StringBuilder();
        sb.append('\uFEFF'); // UTF-8 BOM for Excel compatibility
        sb.append("STT,Mã Đề Tài,Tên Đề Tài,Lĩnh Vực,Mã SV Chủ Nhiệm,GVHD,Kinh Phí Dự Kiến,Trạng Thái\n");

        int stt = 1;
        for (DeTaiClientDTO dt : list) {
            sb.append(stt++).append(",")
              .append(escapeCsv(dt.getMaDeTai())).append(",")
              .append(escapeCsv(dt.getTenDeTai())).append(",")
              .append(escapeCsv(dt.getLinhVuc())).append(",")
              .append(escapeCsv(dt.getMaSvChuNhiem())).append(",")
              .append(escapeCsv(dt.getTenGvhd())).append(",")
              .append(dt.getKinhPhiDuKien() != null ? dt.getKinhPhiDuKien().longValue() : 0).append(",")
              .append(escapeCsv(dt.getTrangThai())).append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Scheduled(fixedRate = 60000)
    @CacheEvict(value = {"thongKeTrangThai", "tiLeKetQua"}, allEntries = true)
    public void evictAnalyticsCaches() {
        log.debug("Expired analytics caches (60s TTL)");
    }
}
