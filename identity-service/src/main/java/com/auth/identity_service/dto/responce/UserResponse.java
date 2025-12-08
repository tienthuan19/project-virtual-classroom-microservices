package com.auth.identity_service.dto.responce;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private LocalDateTime lastLogin;
    private Set<String> roles;
}