package com.lms.lms_backend.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class SubmissionRequest {
    private List<AnswerRequest> answers;

    @Data
    public static class AnswerRequest {
        private String questionId;
        private String studentAnswer;
    }
}