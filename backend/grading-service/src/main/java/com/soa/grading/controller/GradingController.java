package com.soa.grading.controller;

import com.soa.grading.dto.ApiResponse;
import com.soa.grading.dto.GradingDTOs.*;
import com.soa.grading.service.interfaces.IGradingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/diem")
public class GradingController {

    private final IGradingService gradingService;

    public GradingController(IGradingService gradingService) {
        this.gradingService = gradingService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DiemDTO>> nhapDiem(@RequestBody NhapDiemRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Nhập điểm thành công", gradingService.nhapDiem(req)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DiemDTO>> suaDiem(@PathVariable Long id, @RequestBody NhapDiemRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật điểm thành công", gradingService.suaDiem(id, req)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DiemDTO>>> getDiemByDeTai(@RequestParam String maDeTai) {
        return ResponseEntity.ok(ApiResponse.ok(gradingService.getDiemByDeTai(maDeTai)));
    }

    @GetMapping("/trung-binh/{maDeTai}")
    public ResponseEntity<ApiResponse<DiemTrungBinhDTO>> getDiemTrungBinh(@PathVariable String maDeTai) {
        return ResponseEntity.ok(ApiResponse.ok(gradingService.tinhDiemTrungBinh(maDeTai)));
    }
}
