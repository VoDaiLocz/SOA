package com.soa.detai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCaching
@EnableFeignClients
public class DeTaiServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeTaiServiceApplication.class, args);
    }
}
