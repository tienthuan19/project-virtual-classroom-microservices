package com.lms.lms_backend.dto.rabbitmqDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EssayAnswerDto {
    private String question_id;
    private String question_text;
    private String model_answer;
    private String student_answer;
    private int weight;
}