package com.lms.lms_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionResponse {
    private String id;
    private String content;
    private Integer score;
    // LƯU Ý: Không đưa modelAnswer vào đây nếu dành cho học sinh làm bài
}
