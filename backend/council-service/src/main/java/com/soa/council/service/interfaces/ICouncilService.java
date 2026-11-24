package com.soa.council.service.interfaces;

import com.soa.council.dto.CouncilDTOs.*;
import java.util.List;

public interface ICouncilService {
    HoiDongDTO taoHoiDong(TaoHoiDongRequest req);
    List<HoiDongDTO> getAllHoiDong();
    HoiDongDTO getHoiDongByMa(String maHoiDong);
    ThanhVienHoiDongDTO phanCongGiangVien(String maHoiDong, PhanCongGVRequest req);
    PhanCongDeTaiDTO ganDeTai(String maHoiDong, GanDeTaiRequest req);
}
