package com.soa.detai.service;

import com.soa.detai.dto.DeTaiDTOs.*;
import java.util.List;

public interface DeTaiService {
    List<DeTaiDTO> getAllDeTai(String trangThai);
    DeTaiDTO getDeTaiByMa(String maDeTai);
    DeTaiDTO createDeTai(TaoDeTaiRequest req);
    DeTaiDTO updateDeTai(String maDeTai, TaoDeTaiRequest req);
    void deleteDeTai(String maDeTai);
    DeTaiDTO duyetDeTai(String maDeTai);
    DeTaiDTO huyDeTai(String maDeTai);
    DeTaiDTO giaHanDeTai(String maDeTai);

    DangKyDTO dangKyDeTai(DangKyRequest req);
    void huyDangKy(Long id);
}
