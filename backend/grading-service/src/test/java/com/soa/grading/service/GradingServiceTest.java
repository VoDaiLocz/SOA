package com.soa.grading.service;

import com.soa.grading.dto.GradingDTOs.*;
import com.soa.grading.entity.Diem;
import com.soa.grading.repository.DiemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GradingServiceTest {

    @Mock private DiemRepository diemRepository;

    @InjectMocks
    private GradingService gradingService;

    @Test
    void nhapDiemRejectsOutOfRangeScore() {
        NhapDiemRequest req = new NhapDiemRequest();
        req.setMaDeTai("DT01");
        req.setMaGv("GV001");
        req.setDiemSo(10.5);

        assertThrows(IllegalArgumentException.class, () -> gradingService.nhapDiem(req));
    }

    @Test
    void tinhDiemTrungBinhComputesAverageAndRank() {
        Diem d1 = new Diem("DT01", "GV001", "GV 1", "DE_CUONG", 8.0, "Tot");
        Diem d2 = new Diem("DT01", "GV002", "GV 2", "DE_CUONG", 9.0, "Xuat sac");

        when(diemRepository.findByMaDeTai("DT01")).thenReturn(List.of(d1, d2));

        DiemTrungBinhDTO avg = gradingService.tinhDiemTrungBinh("DT01");
        assertEquals(8.5, avg.getDiemTrungBinh());
        assertEquals("GIOI", avg.getXepLoai());
        assertEquals(2, avg.getSoLuongGiangVienCham());
    }

    @Test
    void tinhDiemTrungBinhHandlesEmptyList() {
        when(diemRepository.findByMaDeTai("DT99")).thenReturn(Collections.emptyList());

        DiemTrungBinhDTO avg = gradingService.tinhDiemTrungBinh("DT99");
        assertEquals(0.0, avg.getDiemTrungBinh());
        assertEquals("CHUA_CHAM", avg.getXepLoai());
    }
}
