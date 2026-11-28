package com.soa.detai.controller;

import com.soa.detai.dto.ApiResponse;
import com.soa.detai.dto.DeTaiDTOs.*;
import com.soa.detai.service.interfaces.IDeTaiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/detai")
public class DeTaiController {

    private final IDeTaiService deTaiService;

    public DeTaiController(IDeTaiService deTaiService) {
        this.deTaiService = deTaiService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DeTaiDTO>>> getAllDeTai(@RequestParam(required = false) String trangThai) {
        return ResponseEntity.ok(ApiResponse.ok(deTaiService.getAllDeTai(trangThai)));
    }

    @GetMapping("/{maDeTai}")
    public ResponseEntity<ApiResponse<DeTaiDTO>> getDeTaiByMa(@PathVariable String maDeTai) {
        return ResponseEntity.ok(ApiResponse.ok(deTaiService.getDeTaiByMa(maDeTai)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeTaiDTO>> createDeTai(@RequestBody TaoDeTaiRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo đề tài thành công", deTaiService.createDeTai(req)));
    }

    @PutMapping("/{maDeTai}")
    public ResponseEntity<ApiResponse<DeTaiDTO>> updateDeTai(@PathVariable String maDeTai, @RequestBody TaoDeTaiRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Cập nhật đề tài thành công", deTaiService.updateDeTai(maDeTai, req)));
    }

    @DeleteMapping("/{maDeTai}")
    public ResponseEntity<ApiResponse<Void>> deleteDeTai(@PathVariable String maDeTai) {
        deTaiService.deleteDeTai(maDeTai);
        return ResponseEntity.ok(ApiResponse.ok("Xóa đề tài thành công", null));
    }

    @PostMapping("/{maDeTai}/duyet")
    public ResponseEntity<ApiResponse<DeTaiDTO>> duyetDeTai(@PathVariable String maDeTai) {
        return ResponseEntity.ok(ApiResponse.ok("Duyệt đề tài thành công", deTaiService.duyetDeTai(maDeTai)));
    }

    @PostMapping("/{maDeTai}/huy")
    public ResponseEntity<ApiResponse<DeTaiDTO>> huyDeTai(@PathVariable String maDeTai) {
        return ResponseEntity.ok(ApiResponse.ok("Hủy đề tài thành công", deTaiService.huyDeTai(maDeTai)));
    }

    @PostMapping("/{maDeTai}/gia-han")
    public ResponseEntity<ApiResponse<DeTaiDTO>> giaHanDeTai(@PathVariable String maDeTai) {
        return ResponseEntity.ok(ApiResponse.ok("Gia hạn đề tài thành công", deTaiService.giaHanDeTai(maDeTai)));
    }

    @PostMapping("/dang-ky")
    public ResponseEntity<ApiResponse<DangKyDTO>> dangKyDeTai(
            @RequestBody DangKyRequest req,
            @RequestHeader(value = "X-Auth-Role", required = false) String authRole,
            @RequestHeader(value = "X-Auth-MaDinhDanh", required = false) String authMaSv) {
        if ("SINH_VIEN".equals(authRole) && authMaSv != null && !authMaSv.isBlank() && !authMaSv.equals(req.getMaSv())) {
            throw new SecurityException("Không được phép đăng ký thay cho sinh viên khác");
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Đăng ký giữ chỗ thành công", deTaiService.dangKyDeTai(req)));
    }

    @DeleteMapping("/dang-ky/{id}")
    public ResponseEntity<ApiResponse<Void>> huyDangKy(
            @PathVariable Long id,
            @RequestHeader(value = "X-Auth-Role", required = false) String authRole,
            @RequestHeader(value = "X-Auth-MaDinhDanh", required = false) String authMaSv) {
        deTaiService.huyDangKy(id, authRole, authMaSv);
        return ResponseEntity.ok(ApiResponse.ok("Hủy đăng ký thành công", null));
    }
}
