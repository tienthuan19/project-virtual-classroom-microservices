package com.lms.lms_backend.dto.request;


import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateAssignmentRequest {
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Integer duration;
    private Integer maxScore;
    private List<QuestionRequest> questions;
}