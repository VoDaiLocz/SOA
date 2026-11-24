package com.soa.user.controller;

import com.soa.user.dto.ApiResponse;
import com.soa.user.dto.UserDTOs.*;
import com.soa.user.service.interfaces.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    // --- SINH VIEN ENDPOINTS ---
    @GetMapping("/sinhvien")
    public ResponseEntity<ApiResponse<List<SinhVienDTO>>> getAllSinhVien() {
        return ResponseEntity.ok(ApiResponse.ok(userService.getAllSinhVien()));
    }

    @GetMapping("/sinhvien/{maSv}")
    public ResponseEntity<ApiResponse<SinhVienDTO>> getSinhVienByMa(@PathVariable String maSv) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(userService.getSinhVienByMa(maSv)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/sinhvien")
    public ResponseEntity<ApiResponse<SinhVienDTO>> createSinhVien(@RequestBody SinhVienDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Thêm sinh viên thành công", userService.createSinhVien(dto)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/sinhvien/{maSv}")
    public ResponseEntity<ApiResponse<SinhVienDTO>> updateSinhVien(@PathVariable String maSv, @RequestBody SinhVienDTO dto) {
        try {
            return ResponseEntity.ok(ApiResponse.ok("Cập nhật thông tin sinh viên thành công", userService.updateSinhVien(maSv, dto)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/sinhvien/{maSv}")
    public ResponseEntity<ApiResponse<Void>> deleteSinhVien(@PathVariable String maSv) {
        try {
            userService.deleteSinhVien(maSv);
            return ResponseEntity.ok(ApiResponse.ok("Xóa sinh viên thành công", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    // --- GIANG VIEN ENDPOINTS ---
    @GetMapping("/giangvien")
    public ResponseEntity<ApiResponse<List<GiangVienDTO>>> getAllGiangVien() {
        return ResponseEntity.ok(ApiResponse.ok(userService.getAllGiangVien()));
    }

    @GetMapping("/giangvien/{maGv}")
    public ResponseEntity<ApiResponse<GiangVienDTO>> getGiangVienByMa(@PathVariable String maGv) {
        try {
            return ResponseEntity.ok(ApiResponse.ok(userService.getGiangVienByMa(maGv)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/giangvien")
    public ResponseEntity<ApiResponse<GiangVienDTO>> createGiangVien(@RequestBody GiangVienDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Thêm giảng viên thành công", userService.createGiangVien(dto)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(e.getMessage()));
        }
    }

    // --- KHOA & LOP ENDPOINTS ---
    @GetMapping("/khoa")
    public ResponseEntity<ApiResponse<List<KhoaDTO>>> getAllKhoa() {
        return ResponseEntity.ok(ApiResponse.ok(userService.getAllKhoa()));
    }

    @GetMapping("/lop")
    public ResponseEntity<ApiResponse<List<LopDTO>>> getLopByKhoa(@RequestParam(required = false) String maKhoa) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getLopByKhoa(maKhoa)));
    }
}
