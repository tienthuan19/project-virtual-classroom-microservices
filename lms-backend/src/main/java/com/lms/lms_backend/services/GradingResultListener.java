package com.lms.lms_backend.services;

import com.lms.lms_backend.dto.rabbitmqDto.GradingResultDto;
import com.lms.lms_backend.dto.rabbitmqDto.GradingDetailDto;
import com.lms.lms_backend.models.Submission;
import com.lms.lms_backend.models.SubmissionDetail;
import com.lms.lms_backend.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 👇 THÊM IMPORT NÀY ĐỂ FIX LỖI cannot find symbol class UUID
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradingResultListener {

    private final SubmissionRepository submissionRepository;

    @RabbitListener(queues = "${rabbitmq.queue.result}")
    @Transactional
    public void receiveGradingResult(GradingResultDto result) {
        System.out.println("<<< Received result from AI: " + result);

        // 1. Tìm Submission
        // LỖI TYPE: result.getSubmissionId() là UUID, nhưng repo cần String
        // FIX: Thêm .toString()
        Submission submission = submissionRepository.findById(result.getSubmissionId().toString())
                .orElse(null);

        if (submission == null) {
            System.err.println("!!! Submission not found: " + result.getSubmissionId());
            return;
        }

        // 2. Cập nhật thông tin tổng quan
        submission.setTotalScore(result.getScoreAi());

        // FIX LỖI setFeedback: Đã thêm field này vào model Submission ở Bước 1
        submission.setFeedback(result.getFeedback());

        // 3. Cập nhật chi tiết từng câu (Details)
        if (result.getDetails() != null && !result.getDetails().isEmpty()) {

            // Map danh sách SubmissionDetail hiện có theo QuestionID (String)
            // submissionDetails lấy từ DB, question.getId() trả về String
            Map<String, SubmissionDetail> detailMap = submission.getSubmissionDetails().stream()
                    .collect(Collectors.toMap(
                            d -> d.getQuestion().getId(), // Key là String (ID câu hỏi)
                            d -> d
                    ));

            // Duyệt qua kết quả từ AI gửi về
            for (GradingDetailDto aiDetail : result.getDetails()) {

                // LỖI TYPE: aiDetail.getQuestionId() là UUID, Map key là String
                // FIX: Thêm .toString()
                String questionIdStr = aiDetail.getQuestionId().toString();

                SubmissionDetail dbDetail = detailMap.get(questionIdStr);

                if (dbDetail != null) {
                    dbDetail.setScoreAwarded(aiDetail.getScore());
                    dbDetail.setAiFeedback(aiDetail.getFeedback());
                }
            }
        }

        submissionRepository.save(submission);
        System.out.println(">>> Updated submission & details for ID: " + submission.getId());
    }
}