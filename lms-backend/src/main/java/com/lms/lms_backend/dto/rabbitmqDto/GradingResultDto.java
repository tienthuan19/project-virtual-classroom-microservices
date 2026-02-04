package com.lms.lms_backend.dto.rabbitmqDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
public class GradingResultDto {
    private UUID submissionId;
    private Double scoreAi;
    private String feedback;
    private List<GradingDetailDto> details;
}