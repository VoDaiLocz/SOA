package com.soa.detai.service;

import com.soa.detai.client.UserClient;
import com.soa.detai.dto.ApiResponse;
import com.soa.detai.dto.DeTaiDTOs.*;
import com.soa.detai.entity.DangKy;
import com.soa.detai.entity.DeTai;
import com.soa.detai.repository.DangKyRepository;
import com.soa.detai.repository.DeTaiRepository;
import com.soa.detai.service.interfaces.IDeTaiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DeTaiService implements IDeTaiService {

    private static final Logger log = LoggerFactory.getLogger(DeTaiService.class);
    private static final Set<String> OPEN_FOR_REGISTRATION = Set.of("CHO_DUYET", "DA_DUYET", "DANG_THUC_HIEN");

    private final DeTaiRepository deTaiRepository;
    private final DangKyRepository dangKyRepository;
    private final UserClient userClient;

    public DeTaiService(DeTaiRepository deTaiRepository, DangKyRepository dangKyRepository, UserClient userClient) {
        this.deTaiRepository = deTaiRepository;
        this.dangKyRepository = dangKyRepository;
        this.userClient = userClient;
    }

    private DeTaiDTO toDto(DeTai dt) {
        return new DeTaiDTO(dt.getMaDeTai(), dt.getTenDeTai(), dt.getMoTa(), dt.getLinhVuc(), dt.getKinhPhiDuKien(),
                dt.getMaSvChuNhiem(), dt.getMaGvhd(), dt.getTenGvhd(), dt.getSoLuongHienTai(), dt.getSoLuongToiDa(),
                dt.getTrangThai(), dt.getNgayTao());
    }

    private DeTai findDeTaiOrThrow(String maDeTai) {
        return deTaiRepository.findByMaDeTai(maDeTai)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đề tài: " + maDeTai));
    }

    private void verifyActiveStudent(String maSv) {
        try {
            ApiResponse<SinhVienClientDTO> svRes = userClient.getSinhVienByMa(maSv);
            if (svRes == null || svRes.getData() == null || !"DANG_HOC".equals(svRes.getData().getTrangThai())) {
                throw new IllegalArgumentException("Sinh viên không hợp lệ hoặc đã bị khóa");
            }
        } catch (feign.FeignException.NotFound e) {
            throw new IllegalArgumentException("Không tìm thấy sinh viên trong hệ thống: " + maSv);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Không thể kết nối đến user-service để xác thực sinh viên: " + e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "danhSachDeTai", key = "#trangThai != null ? #trangThai : 'ALL'")
    public List<DeTaiDTO> getAllDeTai(String trangThai) {
        log.info("Querying Database for DeTai with trangThai: {}", trangThai);
        List<DeTai> list = (trangThai != null && !trangThai.trim().isEmpty())
                ? deTaiRepository.findByTrangThai(trangThai)
                : deTaiRepository.findAll();
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public DeTaiDTO getDeTaiByMa(String maDeTai) {
        return toDto(findDeTaiOrThrow(maDeTai));
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public DeTaiDTO createDeTai(TaoDeTaiRequest req) {
        if (deTaiRepository.existsByMaDeTai(req.getMaDeTai())) {
            throw new IllegalArgumentException("Mã đề tài đã tồn tại: " + req.getMaDeTai());
        }
        int maxMembers = req.getSoLuongToiDa() != null ? req.getSoLuongToiDa() : 3;
        if (maxMembers < 1) {
            throw new IllegalArgumentException("Số lượng thành viên tối đa phải lớn hơn 0");
        }
        double budget = req.getKinhPhiDuKien() != null ? req.getKinhPhiDuKien() : 15000000.0;
        if (budget < 0) {
            throw new IllegalArgumentException("Kinh phí dự kiến không được âm");
        }

        if (req.getMaSvChuNhiem() != null && !req.getMaSvChuNhiem().isBlank()) {
            verifyActiveStudent(req.getMaSvChuNhiem().trim());
        }

        DeTai dt = new DeTai();
        dt.setMaDeTai(req.getMaDeTai());
        dt.setTenDeTai(req.getTenDeTai());
        dt.setMoTa(req.getMoTa());
        dt.setLinhVuc(req.getLinhVuc());
        dt.setKinhPhiDuKien(budget);
        dt.setMaSvChuNhiem(req.getMaSvChuNhiem());
        dt.setMaGvhd(req.getMaGvhd());
        dt.setTenGvhd(req.getTenGvhd());
        dt.setSoLuongHienTai(1);
        dt.setSoLuongToiDa(maxMembers);
        dt.setTrangThai("CHO_DUYET");

        DeTai saved = deTaiRepository.save(dt);

        if (req.getMaSvChuNhiem() != null && !req.getMaSvChuNhiem().isBlank()) {
            DangKy dk = new DangKy();
            dk.setMaDeTai(saved.getMaDeTai());
            dk.setMaSv(req.getMaSvChuNhiem().trim());
            dk.setVaiTroThanhVien("CHU_NHIEM");
            dk.setTrangThai("XAC_NHAN");
            dangKyRepository.save(dk);
        }

        return toDto(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public DeTaiDTO updateDeTai(String maDeTai, TaoDeTaiRequest req) {
        DeTai dt = findDeTaiOrThrow(maDeTai);
        if (!"CHO_DUYET".equals(dt.getTrangThai())) {
            throw new IllegalStateException("Chỉ được sửa đề tài khi đang ở trạng thái 'CHO_DUYET'");
        }

        if (req.getTenDeTai() != null) dt.setTenDeTai(req.getTenDeTai());
        if (req.getMoTa() != null) dt.setMoTa(req.getMoTa());
        if (req.getLinhVuc() != null) dt.setLinhVuc(req.getLinhVuc());
        if (req.getKinhPhiDuKien() != null) dt.setKinhPhiDuKien(req.getKinhPhiDuKien());

        return toDto(deTaiRepository.save(dt));
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public void deleteDeTai(String maDeTai) {
        DeTai dt = findDeTaiOrThrow(maDeTai);
        dangKyRepository.deleteByMaDeTai(maDeTai);
        deTaiRepository.delete(dt);
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public DeTaiDTO duyetDeTai(String maDeTai) {
        DeTai dt = findDeTaiOrThrow(maDeTai);
        if (!"CHO_DUYET".equals(dt.getTrangThai())) {
            throw new IllegalStateException("Chỉ có thể duyệt đề tài đang ở trạng thái CHO_DUYET");
        }
        dt.setTrangThai("DA_DUYET");
        return toDto(deTaiRepository.save(dt));
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public DeTaiDTO huyDeTai(String maDeTai) {
        DeTai dt = findDeTaiOrThrow(maDeTai);
        if ("DA_NGHIEM_THU".equals(dt.getTrangThai()) || "DA_HUY".equals(dt.getTrangThai())) {
            throw new IllegalStateException("Không thể hủy đề tài đã nghiệm thu hoặc đã hủy");
        }
        dt.setTrangThai("DA_HUY");
        return toDto(deTaiRepository.save(dt));
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public DeTaiDTO giaHanDeTai(String maDeTai) {
        DeTai dt = findDeTaiOrThrow(maDeTai);
        if (!"DA_DUYET".equals(dt.getTrangThai()) && !"DANG_THUC_HIEN".equals(dt.getTrangThai())) {
            throw new IllegalStateException("Chỉ có thể gia hạn đề tài đang triển khai hoặc đã duyệt");
        }
        dt.setTrangThai("DANG_THUC_HIEN");
        return toDto(deTaiRepository.save(dt));
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public DangKyDTO dangKyDeTai(DangKyRequest req) {
        DeTai dt = findDeTaiOrThrow(req.getMaDeTai());

        if (!OPEN_FOR_REGISTRATION.contains(dt.getTrangThai())) {
            throw new IllegalStateException("Đề tài hiện không mở đăng ký (trạng thái: " + dt.getTrangThai() + ")");
        }
        if (dt.getSoLuongHienTai() >= dt.getSoLuongToiDa()) {
            throw new IllegalStateException("Đề tài đã đủ số lượng thành viên tối đa");
        }
        if (dangKyRepository.existsByMaSvAndMaDeTai(req.getMaSv(), req.getMaDeTai())) {
            throw new IllegalStateException("Sinh viên đã đăng ký tham gia đề tài này rồi");
        }

        verifyActiveStudent(req.getMaSv());

        DangKy dk = new DangKy();
        dk.setMaDeTai(req.getMaDeTai());
        dk.setMaSv(req.getMaSv());
        dk.setVaiTroThanhVien("THANH_VIEN");
        dk.setTrangThai("XAC_NHAN");

        DangKy saved = dangKyRepository.save(dk);

        dt.setSoLuongHienTai(dt.getSoLuongHienTai() + 1);
        deTaiRepository.save(dt);

        return new DangKyDTO(saved.getId(), saved.getMaDeTai(), saved.getMaSv(), saved.getVaiTroThanhVien(), saved.getTrangThai(), saved.getNgayDangKy());
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public void huyDangKy(Long id) {
        huyDangKy(id, null, null);
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public void huyDangKy(Long id, String authRole, String authMaSv) {
        DangKy dk = dangKyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lượt đăng ký: " + id));

        if ("SINH_VIEN".equals(authRole) && authMaSv != null && !authMaSv.isBlank() && !authMaSv.equals(dk.getMaSv())) {
            throw new SecurityException("Không được phép hủy đăng ký của sinh viên khác");
        }
        if ("CHU_NHIEM".equals(dk.getVaiTroThanhVien())) {
            throw new IllegalStateException("Chủ nhiệm đề tài không thể tự rút đăng ký; hãy hủy hoặc chuyển nhượng đề tài");
        }

        deTaiRepository.findByMaDeTai(dk.getMaDeTai()).ifPresent(dt -> {
            if (dt.getSoLuongHienTai() > 1) {
                dt.setSoLuongHienTai(dt.getSoLuongHienTai() - 1);
                deTaiRepository.save(dt);
            }
        });

        dangKyRepository.delete(dk);
    }
}
