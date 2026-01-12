package com.lms.lms_backend.dto.rabbitmqDto;

import lombok.Data;

import java.util.UUID;

@Data
public class GradingDetailDto {
    private UUID questionId;
    private Double score;
    private String feedback;
}
