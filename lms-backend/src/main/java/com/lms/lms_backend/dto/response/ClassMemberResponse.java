package com.lms.lms_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ClassMemberResponse {
    private String id;
    private String name;
    private String email;
    private String role;
    private LocalDateTime joinedAt;
}