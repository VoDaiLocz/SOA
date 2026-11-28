package com.soa.storage.controller;

import com.soa.storage.dto.ApiResponse;
import com.soa.storage.dto.StorageDTOs.TaiLieuDTO;
import com.soa.storage.service.interfaces.IStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tai-lieu")
public class StorageController {

    private final IStorageService storageService;

    public StorageController(IStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<TaiLieuDTO>> uploadFile(
            @RequestParam("maDeTai") String maDeTai,
            @RequestParam(value = "loaiTaiLieu", required = false) String loaiTaiLieu,
            @RequestParam(value = "nguoiUpload", required = false) String nguoiUpload,
            @RequestParam("file") MultipartFile file) {
        TaiLieuDTO dto = storageService.uploadFile(maDeTai, loaiTaiLieu, nguoiUpload, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Upload tài liệu thành công", dto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaiLieuDTO>>> getTaiLieuByDeTai(@RequestParam String maDeTai) {
        return ResponseEntity.ok(ApiResponse.ok(storageService.getTaiLieuByDeTai(maDeTai)));
    }

    @GetMapping("/{id}/tai-ve")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        TaiLieuDTO info = storageService.getTaiLieuById(id);
        Resource file = storageService.downloadFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + info.getTenFile() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFile(@PathVariable Long id) {
        storageService.deleteFile(id);
        return ResponseEntity.ok(ApiResponse.ok("Xóa tài liệu thành công", null));
    }
}
