package com.lms.lms_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomCardResponse {
    private String id;
    private String name;
    private String classCode;
    private String subject;
    private String description;
    private LocalDateTime createdAt;
    private long numberOfStudents;
    private long numberOfAssignments;
}