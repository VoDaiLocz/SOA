package com.soa.user.service;

import com.soa.user.dto.UserDTOs.*;
import java.util.List;

public interface UserService {
    List<SinhVienDTO> getAllSinhVien();
    SinhVienDTO getSinhVienByMa(String maSv);
    SinhVienDTO createSinhVien(SinhVienDTO dto);
    SinhVienDTO updateSinhVien(String maSv, SinhVienDTO dto);
    void deleteSinhVien(String maSv);

    List<GiangVienDTO> getAllGiangVien();
    GiangVienDTO getGiangVienByMa(String maGv);
    GiangVienDTO createGiangVien(GiangVienDTO dto);

    List<KhoaDTO> getAllKhoa();
    List<LopDTO> getLopByKhoa(String maKhoa);
}
