package com.lms.lms_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class StudentGradeResponse {
    private String assignmentId;
    private String assignmentTitle;
    private Double maxScore;

    private String submissionId;
    private Double score;
    private String feedback;
    private LocalDateTime submittedAt;
    private String status;
}