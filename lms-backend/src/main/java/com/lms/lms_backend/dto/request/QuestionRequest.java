package com.lms.lms_backend.dto.request;

import lombok.Data;

@Data
public class QuestionRequest {
    private String content;
    private String modelAnswer;
    private Integer score;
}