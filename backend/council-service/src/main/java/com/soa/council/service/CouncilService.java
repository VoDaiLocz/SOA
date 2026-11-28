package com.soa.council.service;

import com.soa.council.dto.CouncilDTOs.*;
import com.soa.council.entity.*;
import com.soa.council.repository.*;
import com.soa.council.service.interfaces.ICouncilService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CouncilService implements ICouncilService {

    private final HoiDongRepository hoiDongRepository;
    private final ThanhVienHoiDongRepository thanhVienHoiDongRepository;
    private final PhanCongDeTaiRepository phanCongDeTaiRepository;

    public CouncilService(HoiDongRepository hoiDongRepository, ThanhVienHoiDongRepository thanhVienHoiDongRepository, PhanCongDeTaiRepository phanCongDeTaiRepository) {
        this.hoiDongRepository = hoiDongRepository;
        this.thanhVienHoiDongRepository = thanhVienHoiDongRepository;
        this.phanCongDeTaiRepository = phanCongDeTaiRepository;
    }

    private HoiDongDTO toDto(HoiDong hd) {
        List<ThanhVienHoiDongDTO> tvList = thanhVienHoiDongRepository.findByMaHoiDong(hd.getMaHoiDong()).stream()
                .map(tv -> new ThanhVienHoiDongDTO(tv.getId(), tv.getMaHoiDong(), tv.getMaGv(), tv.getTenGv(), tv.getVaiTro()))
                .collect(Collectors.toList());

        List<PhanCongDeTaiDTO> dtList = phanCongDeTaiRepository.findByMaHoiDong(hd.getMaHoiDong()).stream()
                .map(pc -> new PhanCongDeTaiDTO(pc.getId(), pc.getMaHoiDong(), pc.getMaDeTai(), pc.getTenDeTai()))
                .collect(Collectors.toList());

        return new HoiDongDTO(hd.getMaHoiDong(), hd.getTenHoiDong(), hd.getLoaiHoiDong(), hd.getNgayHop(), hd.getDiaDiem(), hd.getTrangThai(), tvList, dtList);
    }

    @Override
    @Transactional
    public HoiDongDTO taoHoiDong(TaoHoiDongRequest req) {
        if (hoiDongRepository.existsByMaHoiDong(req.getMaHoiDong())) {
            throw new IllegalArgumentException("Mã hội đồng đã tồn tại: " + req.getMaHoiDong());
        }

        HoiDong hd = new HoiDong();
        hd.setMaHoiDong(req.getMaHoiDong());
        hd.setTenHoiDong(req.getTenHoiDong());
        hd.setLoaiHoiDong(req.getLoaiHoiDong());
        hd.setNgayHop(req.getNgayHop());
        hd.setDiaDiem(req.getDiaDiem());
        hd.setTrangThai("DANG_HOAT_DONG");

        return toDto(hoiDongRepository.save(hd));
    }

    @Override
    public List<HoiDongDTO> getAllHoiDong() {
        List<HoiDong> hoiDongs = hoiDongRepository.findAll();
        if (hoiDongs.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> maList = hoiDongs.stream().map(HoiDong::getMaHoiDong).collect(Collectors.toList());

        Map<String, List<ThanhVienHoiDongDTO>> tvByHd = thanhVienHoiDongRepository.findByMaHoiDongIn(maList).stream()
                .map(tv -> new ThanhVienHoiDongDTO(tv.getId(), tv.getMaHoiDong(), tv.getMaGv(), tv.getTenGv(), tv.getVaiTro()))
                .collect(Collectors.groupingBy(ThanhVienHoiDongDTO::getMaHoiDong));

        Map<String, List<PhanCongDeTaiDTO>> dtByHd = phanCongDeTaiRepository.findByMaHoiDongIn(maList).stream()
                .map(pc -> new PhanCongDeTaiDTO(pc.getId(), pc.getMaHoiDong(), pc.getMaDeTai(), pc.getTenDeTai()))
                .collect(Collectors.groupingBy(PhanCongDeTaiDTO::getMaHoiDong));

        return hoiDongs.stream()
                .map(hd -> new HoiDongDTO(
                        hd.getMaHoiDong(),
                        hd.getTenHoiDong(),
                        hd.getLoaiHoiDong(),
                        hd.getNgayHop(),
                        hd.getDiaDiem(),
                        hd.getTrangThai(),
                        tvByHd.getOrDefault(hd.getMaHoiDong(), Collections.emptyList()),
                        dtByHd.getOrDefault(hd.getMaHoiDong(), Collections.emptyList())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public HoiDongDTO getHoiDongByMa(String maHoiDong) {
        return hoiDongRepository.findByMaHoiDong(maHoiDong)
                .map(this::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hội đồng có mã: " + maHoiDong));
    }

    @Override
    @Transactional
    public ThanhVienHoiDongDTO phanCongGiangVien(String maHoiDong, PhanCongGVRequest req) {
        if (!hoiDongRepository.existsByMaHoiDong(maHoiDong)) {
            throw new IllegalArgumentException("Hội đồng không tồn tại: " + maHoiDong);
        }

        ThanhVienHoiDong tv = new ThanhVienHoiDong();
        tv.setMaHoiDong(maHoiDong);
        tv.setMaGv(req.getMaGv());
        tv.setTenGv(req.getTenGv());
        tv.setVaiTro(req.getVaiTro());

        ThanhVienHoiDong saved = thanhVienHoiDongRepository.save(tv);
        return new ThanhVienHoiDongDTO(saved.getId(), saved.getMaHoiDong(), saved.getMaGv(), saved.getTenGv(), saved.getVaiTro());
    }

    @Override
    @Transactional
    public PhanCongDeTaiDTO ganDeTai(String maHoiDong, GanDeTaiRequest req) {
        if (!hoiDongRepository.existsByMaHoiDong(maHoiDong)) {
            throw new IllegalArgumentException("Hội đồng không tồn tại: " + maHoiDong);
        }

        PhanCongDeTai pc = new PhanCongDeTai();
        pc.setMaHoiDong(maHoiDong);
        pc.setMaDeTai(req.getMaDeTai());
        pc.setTenDeTai(req.getTenDeTai());

        PhanCongDeTai saved = phanCongDeTaiRepository.save(pc);
        return new PhanCongDeTaiDTO(saved.getId(), saved.getMaHoiDong(), saved.getMaDeTai(), saved.getTenDeTai());
    }
}
