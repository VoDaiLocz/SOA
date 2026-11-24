package com.soa.detai.client;

import com.soa.detai.dto.ApiResponse;
import com.soa.detai.dto.DeTaiDTOs.SinhVienClientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${app.services.user-url:http://localhost:8081}")
public interface UserClient {
    @GetMapping("/api/v1/sinhvien/{maSv}")
    ApiResponse<SinhVienClientDTO> getSinhVienByMa(@PathVariable("maSv") String maSv);
}
