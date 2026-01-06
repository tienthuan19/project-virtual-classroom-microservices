package com.lms.lms_backend.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest; // Spring Boot 3 dùng Jakarta
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Lấy các thuộc tính của Request hiện tại (từ Controller đang xử lý)
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                // Lấy header Authorization (Bearer token)
                String token = request.getHeader("Authorization");

                // Nếu có token, gắn nó vào header của Feign Request sắp gửi đi
                if (token != null) {
                    requestTemplate.header("Authorization", token);
                }
            }
        };
    }
}
