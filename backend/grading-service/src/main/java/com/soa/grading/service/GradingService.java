package com.soa.grading.service;

import com.soa.grading.dto.GradingDTOs.*;
import com.soa.grading.entity.Diem;
import com.soa.grading.repository.DiemRepository;
import com.soa.grading.service.interfaces.IGradingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradingService implements IGradingService {

    private final DiemRepository diemRepository;

    public GradingService(DiemRepository diemRepository) {
        this.diemRepository = diemRepository;
    }

    private DiemDTO toDto(Diem d) {
        return new DiemDTO(
                d.getId(),
                d.getMaDeTai(),
                d.getMaGv(),
                d.getTenGv(),
                d.getLoaiDiem(),
                d.getDiemSo(),
                d.getNhanXet(),
                d.getNgayCham()
        );
    }

    private void validateScore(Double diemSo) {
        if (diemSo == null || diemSo < 0.0 || diemSo > 10.0) {
            throw new IllegalArgumentException("Điểm số phải nằm trong khoảng từ 0.0 đến 10.0");
        }
    }

    @Override
    @Transactional
    public DiemDTO nhapDiem(NhapDiemRequest req) {
        validateScore(req.getDiemSo());
        String loaiDiem = req.getLoaiDiem() != null ? req.getLoaiDiem() : "DE_CUONG";
        if (diemRepository.existsByMaDeTaiAndMaGvAndLoaiDiem(req.getMaDeTai(), req.getMaGv(), loaiDiem)) {
            throw new IllegalArgumentException("Giảng viên " + req.getMaGv() + " đã chấm điểm " + loaiDiem + " cho đề tài " + req.getMaDeTai());
        }
        Diem d = new Diem(
                req.getMaDeTai(),
                req.getMaGv(),
                req.getTenGv(),
                loaiDiem,
                req.getDiemSo(),
                req.getNhanXet()
        );
        return toDto(diemRepository.save(d));
    }

    @Override
    @Transactional
    public DiemDTO suaDiem(Long id, NhapDiemRequest req) {
        Diem d = diemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bản ghi điểm: " + id));

        if (req.getDiemSo() != null) {
            validateScore(req.getDiemSo());
            d.setDiemSo(req.getDiemSo());
        }
        if (req.getNhanXet() != null) d.setNhanXet(req.getNhanXet());
        if (req.getLoaiDiem() != null) d.setLoaiDiem(req.getLoaiDiem());

        return toDto(diemRepository.save(d));
    }

    @Override
    public List<DiemDTO> getDiemByDeTai(String maDeTai) {
        return diemRepository.findByMaDeTai(maDeTai).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public DiemTrungBinhDTO tinhDiemTrungBinh(String maDeTai) {
        List<Diem> list = diemRepository.findByMaDeTai(maDeTai);
        if (list.isEmpty()) {
            return new DiemTrungBinhDTO(maDeTai, 0.0, 0, "CHUA_CHAM");
        }

        double sum = list.stream().mapToDouble(Diem::getDiemSo).sum();
        double avg = Math.round((sum / list.size()) * 100.0) / 100.0;

        String xepLoai;
        if (avg >= 9.0) xepLoai = "XUAT_SAC";
        else if (avg >= 8.0) xepLoai = "GIOI";
        else if (avg >= 7.0) xepLoai = "KHA";
        else if (avg >= 5.0) xepLoai = "DAT";
        else xepLoai = "KHONG_DAT";

        return new DiemTrungBinhDTO(maDeTai, avg, list.size(), xepLoai);
    }
}
