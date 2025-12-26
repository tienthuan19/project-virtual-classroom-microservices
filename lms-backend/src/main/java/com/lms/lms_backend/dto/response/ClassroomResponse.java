package com.lms.lms_backend.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomResponse {
    private String id;
    private String name;
    private String classCode;
    private String description;
    private String subject;
    private String creatorId;
    private LocalDateTime createdAt;
}
