package com.soa.user.service.impl;

import com.soa.user.dto.UserDTOs.*;
import com.soa.user.entity.*;
import com.soa.user.repository.*;
import com.soa.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final SinhVienRepository sinhVienRepository;
    private final GiangVienRepository giangVienRepository;
    private final KhoaRepository khoaRepository;
    private final LopRepository lopRepository;

    public UserServiceImpl(SinhVienRepository sinhVienRepository, GiangVienRepository giangVienRepository, KhoaRepository khoaRepository, LopRepository lopRepository) {
        this.sinhVienRepository = sinhVienRepository;
        this.giangVienRepository = giangVienRepository;
        this.khoaRepository = khoaRepository;
        this.lopRepository = lopRepository;
    }

    private SinhVienDTO toDto(SinhVien sv) {
        return new SinhVienDTO(sv.getMaSv(), sv.getHoTen(), sv.getEmail(), sv.getSoDienThoai(), sv.getChuyenNganh(), sv.getKhoa(), sv.getLop(), sv.getGpa(), sv.getTrangThai());
    }

    private GiangVienDTO toDto(GiangVien gv) {
        return new GiangVienDTO(gv.getMaGv(), gv.getHoTen(), gv.getEmail(), gv.getSoDienThoai(), gv.getHocVi(), gv.getKhoa(), gv.getBoMon(), gv.getHuongNghienCuu());
    }

    @Override
    public List<SinhVienDTO> getAllSinhVien() {
        return sinhVienRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public SinhVienDTO getSinhVienByMa(String maSv) {
        return sinhVienRepository.findByMaSv(maSv)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên có mã: " + maSv));
    }

    @Override
    @Transactional
    public SinhVienDTO createSinhVien(SinhVienDTO dto) {
        if (sinhVienRepository.existsByMaSv(dto.getMaSv())) {
            throw new IllegalArgumentException("Mã sinh viên đã tồn tại: " + dto.getMaSv());
        }
        SinhVien sv = new SinhVien();
        sv.setMaSv(dto.getMaSv());
        sv.setHoTen(dto.getHoTen());
        sv.setEmail(dto.getEmail());
        sv.setSoDienThoai(dto.getSoDienThoai());
        sv.setChuyenNganh(dto.getChuyenNganh());
        sv.setKhoa(dto.getKhoa());
        sv.setLop(dto.getLop());
        sv.setGpa(dto.getGpa());
        sv.setTrangThai(dto.getTrangThai() != null ? dto.getTrangThai() : "DANG_HOC");
        return toDto(sinhVienRepository.save(sv));
    }

    @Override
    @Transactional
    public SinhVienDTO updateSinhVien(String maSv, SinhVienDTO dto) {
        SinhVien sv = sinhVienRepository.findByMaSv(maSv)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên có mã: " + maSv));
        if (dto.getHoTen() != null) sv.setHoTen(dto.getHoTen());
        if (dto.getEmail() != null) sv.setEmail(dto.getEmail());
        if (dto.getSoDienThoai() != null) sv.setSoDienThoai(dto.getSoDienThoai());
        if (dto.getGpa() != null) sv.setGpa(dto.getGpa());
        if (dto.getTrangThai() != null) sv.setTrangThai(dto.getTrangThai());
        return toDto(sinhVienRepository.save(sv));
    }

    @Override
    @Transactional
    public void deleteSinhVien(String maSv) {
        if (!sinhVienRepository.existsByMaSv(maSv)) {
            throw new IllegalArgumentException("Không tìm thấy sinh viên có mã: " + maSv);
        }
        sinhVienRepository.deleteByMaSv(maSv);
    }

    @Override
    public List<GiangVienDTO> getAllGiangVien() {
        return giangVienRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public GiangVienDTO getGiangVienByMa(String maGv) {
        return giangVienRepository.findByMaGv(maGv)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giảng viên có mã: " + maGv));
    }

    @Override
    @Transactional
    public GiangVienDTO createGiangVien(GiangVienDTO dto) {
        if (giangVienRepository.existsByMaGv(dto.getMaGv())) {
            throw new IllegalArgumentException("Mã giảng viên đã tồn tại: " + dto.getMaGv());
        }
        GiangVien gv = new GiangVien();
        gv.setMaGv(dto.getMaGv());
        gv.setHoTen(dto.getHoTen());
        gv.setEmail(dto.getEmail());
        gv.setSoDienThoai(dto.getSoDienThoai());
        gv.setHocVi(dto.getHocVi());
        gv.setKhoa(dto.getKhoa());
        gv.setBoMon(dto.getBoMon());
        gv.setHuongNghienCuu(dto.getHuongNghienCuu());
        return toDto(giangVienRepository.save(gv));
    }

    @Override
    public List<KhoaDTO> getAllKhoa() {
        return khoaRepository.findAll().stream()
                .map(k -> new KhoaDTO(k.getMaKhoa(), k.getTenKhoa()))
                .collect(Collectors.toList());
    }

    @Override
    public List<LopDTO> getLopByKhoa(String maKhoa) {
        List<Lop> list = (maKhoa != null && !maKhoa.isEmpty())
                ? lopRepository.findByMaKhoa(maKhoa)
                : lopRepository.findAll();
        return list.stream()
                .map(l -> new LopDTO(l.getMaLop(), l.getTenLop(), l.getMaKhoa()))
                .collect(Collectors.toList());
    }
}
