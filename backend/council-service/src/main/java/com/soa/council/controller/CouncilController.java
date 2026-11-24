package com.soa.council.controller;

import com.soa.council.dto.ApiResponse;
import com.soa.council.dto.CouncilDTOs.*;
import com.soa.council.service.CouncilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hoi-dong")
public class CouncilController {

    private final CouncilService councilService;

    public CouncilController(CouncilService councilService) {
        this.councilService = councilService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HoiDongDTO>> taoHoiDong(@RequestBody TaoHoiDongRequest req) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Thành lập hội đồng thành công", councilService.taoHoiDong(req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HoiDongDTO>>> getAllHoiDong() {
        return ResponseEntity.ok(ApiResponse.ok(councilService.getAllHoiDong()));
    }

    @GetMapping("/{ma}")
    public ResponseEntity<ApiResponse<HoiDongDTO>> getHoiDongByMa(@PathVariable String ma) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(councilService.getHoiDongByMa(ma)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{ma}/phan-cong")
    public ResponseEntity<ApiResponse<ThanhVienHoiDongDTO>> phanCongGiangVien(@PathVariable String ma, @RequestBody PhanCongGVRequest req) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Phân công giảng viên vào hội đồng thành công", councilService.phanCongGiangVien(ma, req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{ma}/gan-de-tai")
    public ResponseEntity<ApiResponse<PhanCongDeTaiDTO>> ganDeTai(@PathVariable String ma, @RequestBody GanDeTaiRequest req) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Gán đề tài cho hội đồng thành công", councilService.ganDeTai(ma, req)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }
}
