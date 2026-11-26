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
import java.util.stream.Collectors;

@Service
public class DeTaiService implements IDeTaiService {

    private static final Logger log = LoggerFactory.getLogger(DeTaiService.class);

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
        return deTaiRepository.findByMaDeTai(maDeTai)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đề tài mã: " + maDeTai));
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public DeTaiDTO createDeTai(TaoDeTaiRequest req) {
        if (deTaiRepository.existsByMaDeTai(req.getMaDeTai())) {
            throw new IllegalArgumentException("Mã đề tài đã tồn tại: " + req.getMaDeTai());
        }

        DeTai dt = new DeTai();
        dt.setMaDeTai(req.getMaDeTai());
        dt.setTenDeTai(req.getTenDeTai());
        dt.setMoTa(req.getMoTa());
        dt.setLinhVuc(req.getLinhVuc());
        dt.setKinhPhiDuKien(req.getKinhPhiDuKien() != null ? req.getKinhPhiDuKien() : 15000000.0);
        dt.setMaSvChuNhiem(req.getMaSvChuNhiem());
        dt.setMaGvhd(req.getMaGvhd());
        dt.setTenGvhd(req.getTenGvhd());
        dt.setSoLuongHienTai(1);
        dt.setSoLuongToiDa(req.getSoLuongToiDa() != null ? req.getSoLuongToiDa() : 3);
        dt.setTrangThai("CHO_DUYET");

        DeTai saved = deTaiRepository.save(dt);

        if (req.getMaSvChuNhiem() != null && !req.getMaSvChuNhiem().isEmpty()) {
            DangKy dk = new DangKy();
            dk.setMaDeTai(saved.getMaDeTai());
            dk.setMaSv(req.getMaSvChuNhiem());
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
        DeTai dt = deTaiRepository.findByMaDeTai(maDeTai)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đề tài: " + maDeTai));

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
        DeTai dt = deTaiRepository.findByMaDeTai(maDeTai)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đề tài: " + maDeTai));
        deTaiRepository.delete(dt);
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public DeTaiDTO duyetDeTai(String maDeTai) {
        DeTai dt = deTaiRepository.findByMaDeTai(maDeTai)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đề tài: " + maDeTai));
        dt.setTrangThai("DA_DUYET");
        return toDto(deTaiRepository.save(dt));
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public DeTaiDTO huyDeTai(String maDeTai) {
        DeTai dt = deTaiRepository.findByMaDeTai(maDeTai)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đề tài: " + maDeTai));
        dt.setTrangThai("DA_HUY");
        return toDto(deTaiRepository.save(dt));
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public DeTaiDTO giaHanDeTai(String maDeTai) {
        DeTai dt = deTaiRepository.findByMaDeTai(maDeTai)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đề tài: " + maDeTai));
        dt.setTrangThai("DANG_THUC_HIEN");
        return toDto(deTaiRepository.save(dt));
    }

    @Override
    @Transactional
    @CacheEvict(value = "danhSachDeTai", allEntries = true)
    public DangKyDTO dangKyDeTai(DangKyRequest req) {
        DeTai dt = deTaiRepository.findByMaDeTai(req.getMaDeTai())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đề tài: " + req.getMaDeTai()));

        if (dt.getSoLuongHienTai() >= dt.getSoLuongToiDa()) {
            throw new IllegalStateException("Đề tài đã đủ số lượng thành viên tối đa");
        }

        if (dangKyRepository.existsByMaSvAndMaDeTai(req.getMaSv(), req.getMaDeTai())) {
            throw new IllegalStateException("Sinh viên đã đăng ký tham gia đề tài này rồi");
        }

        try {
            ApiResponse<SinhVienClientDTO> svRes = userClient.getSinhVienByMa(req.getMaSv());
            if (svRes == null || svRes.getData() == null || !"DANG_HOC".equals(svRes.getData().getTrangThai())) {
                throw new IllegalArgumentException("Sinh viên không hợp lệ hoặc đã bị khóa");
            }
        } catch (feign.FeignException.NotFound e) {
            throw new IllegalArgumentException("Không tìm thấy sinh viên trong hệ thống: " + req.getMaSv());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Không thể kết nối đến user-service để xác thực sinh viên: " + e.getMessage());
        }

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
        DangKy dk = dangKyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy lượt đăng ký: " + id));

        deTaiRepository.findByMaDeTai(dk.getMaDeTai()).ifPresent(dt -> {
            if (dt.getSoLuongHienTai() > 1) {
                dt.setSoLuongHienTai(dt.getSoLuongHienTai() - 1);
                deTaiRepository.save(dt);
            }
        });

        dangKyRepository.delete(dk);
    }
}
