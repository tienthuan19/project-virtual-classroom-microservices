package com.lms.lms_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AssignmentResponse {
    private String id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Integer duration;
    private Integer maxScore;
    private LocalDateTime createdAt;
}
