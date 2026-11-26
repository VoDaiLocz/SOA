package com.soa.analytics.client;

import com.soa.analytics.dto.AnalyticsDTOs.DeTaiClientDTO;
import com.soa.analytics.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "detai-service", url = "${app.services.detai-url:http://localhost:8082}")
public interface DeTaiClient {
    @GetMapping("/api/v1/detai")
    ApiResponse<List<DeTaiClientDTO>> getAllDeTai();
}
