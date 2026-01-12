package com.lms.lms_backend.dto.rabbitmqDto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class SubmissionMessageDto {
    private String submission_id;
    private String assignment_id;
    private String student_id;
    private String file_url;
    private List<EssayAnswerDto> essay_answers;
    private String submitted_at;
}
