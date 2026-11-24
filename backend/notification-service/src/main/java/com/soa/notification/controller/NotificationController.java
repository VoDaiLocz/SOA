package com.soa.notification.controller;

import com.soa.notification.dto.ApiResponse;
import com.soa.notification.dto.NotificationDTOs.*;
import com.soa.notification.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/thong-bao")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{maNguoiNhan}")
    public ResponseEntity<ApiResponse<List<ThongBaoDTO>>> getThongBao(@PathVariable String maNguoiNhan) {
        return ResponseEntity.ok(ApiResponse.ok(notificationService.getThongBaoByNguoiNhan(maNguoiNhan)));
    }

    @PutMapping("/{id}/da-doc")
    public ResponseEntity<ApiResponse<Void>> danhDauDaDoc(@PathVariable Long id) {
        notificationService.danhDauDaDoc(id);
        return ResponseEntity.ok(ApiResponse.ok("Đã đánh dấu thông báo là đã đọc", null));
    }

    @PostMapping("/gui")
    public ResponseEntity<ApiResponse<ThongBaoDTO>> guiThongBao(@RequestBody GuiThongBaoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok("Gửi thông báo thành công", notificationService.guiThongBao(req)));
    }
}
