package com.soa.detai.service;

import com.soa.detai.client.UserClient;
import com.soa.detai.dto.ApiResponse;
import com.soa.detai.dto.DeTaiDTOs.*;
import com.soa.detai.entity.DangKy;
import com.soa.detai.entity.DeTai;
import com.soa.detai.repository.DangKyRepository;
import com.soa.detai.repository.DeTaiRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeTaiServiceTest {

    @Mock private DeTaiRepository deTaiRepository;
    @Mock private DangKyRepository dangKyRepository;
    @Mock private UserClient userClient;

    @InjectMocks
    private DeTaiService deTaiService;

    @Test
    void dangKyDeTaiSuccessIncrementsMemberCount() {
        DeTai dt = new DeTai();
        dt.setMaDeTai("DT01");
        dt.setSoLuongHienTai(1);
        dt.setSoLuongToiDa(3);

        SinhVienClientDTO sv = new SinhVienClientDTO();
        sv.setMaSv("SV002");
        sv.setTrangThai("DANG_HOC");

        when(deTaiRepository.findByMaDeTai("DT01")).thenReturn(Optional.of(dt));
        when(dangKyRepository.existsByMaSvAndMaDeTai("SV002", "DT01")).thenReturn(false);
        when(userClient.getSinhVienByMa("SV002")).thenReturn(ApiResponse.ok(sv));
        when(dangKyRepository.save(any(DangKy.class))).thenAnswer(inv -> inv.getArgument(0));

        DangKyRequest req = new DangKyRequest();
        req.setMaDeTai("DT01");
        req.setMaSv("SV002");

        DangKyDTO res = deTaiService.dangKyDeTai(req);
        assertEquals("SV002", res.getMaSv());
        assertEquals(2, dt.getSoLuongHienTai());
        verify(deTaiRepository).save(dt);
    }

    @Test
    void huyDangKyThrowsSecurityExceptionOnIdor() {
        DangKy dk = new DangKy();
        dk.setId(10L);
        dk.setMaDeTai("DT01");
        dk.setMaSv("SV001");

        when(dangKyRepository.findById(10L)).thenReturn(Optional.of(dk));

        assertThrows(SecurityException.class, () -> deTaiService.huyDangKy(10L, "SINH_VIEN", "SV999"));
        verify(dangKyRepository, never()).delete(any());
    }
}
