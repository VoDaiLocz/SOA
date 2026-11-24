package com.soa.council.service;

import com.soa.council.dto.CouncilDTOs.*;
import java.util.List;

public interface CouncilService {
    HoiDongDTO taoHoiDong(TaoHoiDongRequest req);
    List<HoiDongDTO> getAllHoiDong();
    HoiDongDTO getHoiDongByMa(String maHoiDong);
    ThanhVienHoiDongDTO phanCongGiangVien(String maHoiDong, PhanCongGVRequest req);
    PhanCongDeTaiDTO ganDeTai(String maHoiDong, GanDeTaiRequest req);
}
