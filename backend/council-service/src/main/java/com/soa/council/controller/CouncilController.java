package com.soa.council.controller;

import com.soa.council.dto.ApiResponse;
import com.soa.council.dto.CouncilDTOs.*;
import com.soa.council.service.interfaces.ICouncilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hoi-dong")
public class CouncilController {

    private final ICouncilService councilService;

    public CouncilController(ICouncilService councilService) {
        this.councilService = councilService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HoiDongDTO>> taoHoiDong(@RequestBody TaoHoiDongRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Thành lập hội đồng thành công", councilService.taoHoiDong(req)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HoiDongDTO>>> getAllHoiDong() {
        return ResponseEntity.ok(ApiResponse.ok(councilService.getAllHoiDong()));
    }

    @GetMapping("/{ma}")
    public ResponseEntity<ApiResponse<HoiDongDTO>> getHoiDongByMa(@PathVariable String ma) {
        return ResponseEntity.ok(ApiResponse.ok(councilService.getHoiDongByMa(ma)));
    }

    @PostMapping("/{ma}/phan-cong")
    public ResponseEntity<ApiResponse<ThanhVienHoiDongDTO>> phanCongGiangVien(@PathVariable String ma, @RequestBody PhanCongGVRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Phân công giảng viên vào hội đồng thành công", councilService.phanCongGiangVien(ma, req)));
    }

    @PostMapping("/{ma}/gan-de-tai")
    public ResponseEntity<ApiResponse<PhanCongDeTaiDTO>> ganDeTai(@PathVariable String ma, @RequestBody GanDeTaiRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Gán đề tài cho hội đồng thành công", councilService.ganDeTai(ma, req)));
    }
}
