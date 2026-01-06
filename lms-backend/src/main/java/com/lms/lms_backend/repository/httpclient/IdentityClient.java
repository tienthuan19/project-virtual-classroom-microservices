package com.lms.lms_backend.repository.httpclient;

import com.lms.lms_backend.dto.response.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "identity-service", url = "http://localhost:8080")
public interface IdentityClient {

    @PostMapping("/api/identity-service/v1/users/bulk-info")
    ApiResponse<List<UserResponse>> getUsersByIds(@RequestBody List<String> userIds);

    // Bạn cần tạo class UserResponse bên lms-backend để hứng dữ liệu trả về
    // (copy fields id, email từ identity-service sang)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class UserResponse {
        private String id;
        private String email;
        private String username;
    }
}
