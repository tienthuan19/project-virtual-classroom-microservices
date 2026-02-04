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

        Submission submission = submissionRepository.findById(result.getSubmissionId().toString())
                .orElse(null);

        if (submission == null) {
            System.err.println("!!! Submission not found: " + result.getSubmissionId());
            return;
        }
        submission.setTotalScore(result.getScoreAi());

        submission.setFeedback(result.getFeedback());

        if (result.getDetails() != null && !result.getDetails().isEmpty()) {

            Map<String, SubmissionDetail> detailMap = submission.getSubmissionDetails().stream()
                    .collect(Collectors.toMap(
                            d -> d.getQuestion().getId(),
                            d -> d
                    ));

            for (GradingDetailDto aiDetail : result.getDetails()) {

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