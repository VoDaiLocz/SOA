package com.soa.grading.service;

import com.soa.grading.dto.GradingDTOs.*;
import java.util.List;

public interface GradingService {
    DiemDTO nhapDiem(NhapDiemRequest req);
    DiemDTO suaDiem(Long id, NhapDiemRequest req);
    List<DiemDTO> getDiemByDeTai(String maDeTai);
    DiemTrungBinhDTO tinhDiemTrungBinh(String maDeTai);
}
