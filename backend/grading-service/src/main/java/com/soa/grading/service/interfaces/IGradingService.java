package com.soa.grading.service.interfaces;

import com.soa.grading.dto.GradingDTOs.*;
import java.util.List;

public interface IGradingService {
    DiemDTO nhapDiem(NhapDiemRequest req);
    DiemDTO suaDiem(Long id, NhapDiemRequest req);
    List<DiemDTO> getDiemByDeTai(String maDeTai);
    DiemTrungBinhDTO tinhDiemTrungBinh(String maDeTai);
}
