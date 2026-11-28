package com.soa.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * GlobalFilter xác thực JWT tại API Gateway và chuyển tiếp thông tin người dùng
 * qua các header nội bộ (X-Auth-User, X-Auth-Role, X-Auth-MaDinhDanh).
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        HttpMethod method = request.getMethod();

        if (HttpMethod.OPTIONS.equals(method) || isPublicEndpoint(path, method)) {
            return chain.filter(exchange);
        }

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return unauthorized(exchange, "Thiếu token xác thực");
        }

        String token = authHeader.substring(7).trim();
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-Auth-User", safeStr(claims.getSubject()))
                    .header("X-Auth-Role", safeStr(claims.get("role", String.class)))
                    .header("X-Auth-MaDinhDanh", safeStr(claims.get("maSv", String.class)))
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception e) {
            return unauthorized(exchange, "Token không hợp lệ hoặc đã hết hạn");
        }
    }

    private boolean isPublicEndpoint(String path, HttpMethod method) {
        if (path.startsWith("/api/v1/auth/") || path.startsWith("/actuator/")) {
            return true;
        }
        // Cho phép tra cứu công khai (GET) trừ thông báo cá nhân
        return HttpMethod.GET.equals(method) && !path.startsWith("/api/v1/thong-bao");
    }

    private String safeStr(String value) {
        return value != null ? value : "";
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = "{\"success\":false,\"message\":\"" + message + "\"}";
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
