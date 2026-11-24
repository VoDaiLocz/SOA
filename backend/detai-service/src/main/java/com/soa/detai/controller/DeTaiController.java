package com.soa.detai.controller;

import com.soa.detai.dto.ApiResponse;
import com.soa.detai.dto.DeTaiDTOs.*;
import com.soa.detai.service.DeTaiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/detai")
public class DeTaiController {

    private final DeTaiService deTaiService;

    public DeTaiController(DeTaiService deTaiService) {
        this.deTaiService = deTaiService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeTaiDTO>>> getAllDeTai(@RequestParam(required = false) String trangThai) {
        return ResponseEntity.ok(ApiResponse.ok(deTaiService.getAllDeTai(trangThai)));
    }

    @GetMapping("/{maDeTai}")
    public ResponseEntity<ApiResponse<DeTaiDTO>> getDeTaiByMa(@PathVariable String maDeTai) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(deTaiService.getDeTaiByMa(maDeTai)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeTaiDTO>> createDeTai(@RequestBody TaoDeTaiRequest req) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Tạo đề tài thành công", deTaiService.createDeTai(req)));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{maDeTai}")
    public ResponseEntity<ApiResponse<DeTaiDTO>> updateDeTai(@PathVariable String maDeTai, @RequestBody TaoDeTaiRequest req) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Cập nhật đề tài thành công", deTaiService.updateDeTai(maDeTai, req)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{maDeTai}")
    public ResponseEntity<ApiResponse<Void>> deleteDeTai(@PathVariable String maDeTai) {
        try {
            deTaiService.deleteDeTai(maDeTai);
            return ResponseEntity.ok(ApiResponse.ok("Xóa đề tài thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{maDeTai}/duyet")
    public ResponseEntity<ApiResponse<DeTaiDTO>> duyetDeTai(@PathVariable String maDeTai) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Duyệt đề tài thành công", deTaiService.duyetDeTai(maDeTai)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{maDeTai}/huy")
    public ResponseEntity<ApiResponse<DeTaiDTO>> huyDeTai(@PathVariable String maDeTai) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Hủy đề tài thành công", deTaiService.huyDeTai(maDeTai)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{maDeTai}/gia-han")
    public ResponseEntity<ApiResponse<DeTaiDTO>> giaHanDeTai(@PathVariable String maDeTai) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Gia hạn đề tài thành công", deTaiService.giaHanDeTai(maDeTai)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/dang-ky")
    public ResponseEntity<ApiResponse<DangKyDTO>> dangKyDeTai(@RequestBody DangKyRequest req) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Đăng ký giữ chỗ thành công", deTaiService.dangKyDeTai(req)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/dang-ky/{id}")
    public ResponseEntity<ApiResponse<Void>> huyDangKy(@PathVariable Long id) {
        try {
            deTaiService.huyDangKy(id);
            return ResponseEntity.ok(ApiResponse.ok("Hủy đăng ký thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }
}
