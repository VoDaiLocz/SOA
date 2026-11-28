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
import java.util.Set;

/**
 * GlobalFilter xác thực JWT, kiểm tra phân quyền RBAC tại API Gateway
 * và chuyển tiếp header danh tính đáng tin cậy xuống các microservice.
 */
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private static final Set<String> ADMIN_ROLES = Set.of("ADMIN", "QUAN_LY");
    private static final Set<String> STAFF_ROLES = Set.of("ADMIN", "QUAN_LY", "GIANG_VIEN");

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
            return errorResponse(exchange, HttpStatus.UNAUTHORIZED, "Thiếu token xác thực");
        }

        String token = authHeader.substring(7).trim();
        Claims claims;
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return errorResponse(exchange, HttpStatus.UNAUTHORIZED, "Token không hợp lệ hoặc đã hết hạn");
        }

        String role = safeStr(claims.get("role", String.class));
        if (!isAuthorized(path, method, role)) {
            return errorResponse(exchange, HttpStatus.FORBIDDEN, "Bạn không có quyền thực hiện thao tác này");
        }

        // Xóa các header X-Auth-* do client tự gửi lên để chống giả mạo trước khi gán giá trị từ JWT
        ServerHttpRequest mutatedRequest = request.mutate()
                .headers(headers -> {
                    headers.remove("X-Auth-User");
                    headers.remove("X-Auth-Role");
                    headers.remove("X-Auth-MaDinhDanh");
                })
                .header("X-Auth-User", safeStr(claims.getSubject()))
                .header("X-Auth-Role", role)
                .header("X-Auth-MaDinhDanh", safeStr(claims.get("maSv", String.class)))
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    private boolean isPublicEndpoint(String path, HttpMethod method) {
        if (path.startsWith("/api/v1/auth/") || path.startsWith("/actuator/")) {
            return true;
        }
        return HttpMethod.GET.equals(method) && !path.startsWith("/api/v1/thong-bao");
    }

    private boolean isAuthorized(String path, HttpMethod method, String role) {
        if (path.startsWith("/api/v1/sinhvien") || path.startsWith("/api/v1/giangvien")
                || path.startsWith("/api/v1/khoa") || path.startsWith("/api/v1/lop")) {
            return ADMIN_ROLES.contains(role);
        }
        if (path.startsWith("/api/v1/hoi-dong") || path.startsWith("/api/v1/diem")) {
            return STAFF_ROLES.contains(role);
        }
        if (path.startsWith("/api/v1/detai/")) {
            if (path.endsWith("/duyet") || path.endsWith("/huy") || path.endsWith("/gia-han")) {
                return STAFF_ROLES.contains(role);
            }
            if (HttpMethod.DELETE.equals(method) && !path.startsWith("/api/v1/detai/dang-ky")) {
                return ADMIN_ROLES.contains(role);
            }
        }
        return true;
    }

    private String safeStr(String value) {
        return value != null ? value : "";
    }

    private Mono<Void> errorResponse(ServerWebExchange exchange, HttpStatus status, String message) {
        exchange.getResponse().setStatusCode(status);
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
