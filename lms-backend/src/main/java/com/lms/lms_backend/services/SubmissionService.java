package com.lms.lms_backend.services;

import com.lms.lms_backend.dto.rabbitmqDto.EssayAnswerDto;
import com.lms.lms_backend.dto.rabbitmqDto.SubmissionMessageDto;
import com.lms.lms_backend.dto.request.SubmissionRequest;
import com.lms.lms_backend.dto.response.StudentGradeResponse;
import com.lms.lms_backend.models.Assignment;
import com.lms.lms_backend.models.Question;
import com.lms.lms_backend.models.Submission;
import com.lms.lms_backend.models.SubmissionDetail;
import com.lms.lms_backend.repository.AssignmentRepository;
import com.lms.lms_backend.repository.SubmissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.queue.submission}")
    private String submissionQueue;

    @Transactional
    public String submitAssignment(String assignmentId, SubmissionRequest request) {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 1
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // 2
        if (assignment.getDueDate() != null && LocalDateTime.now().isAfter(assignment.getDueDate())) {
            throw new RuntimeException("Submission deadline has passed");
        }

        // 3
        if (submissionRepository.existsByAssignmentIdAndStudentId(assignmentId, studentId)) {
            throw new RuntimeException("You have already submitted this assignment");
        }

        // 4
        Submission submission = Submission.builder()
                .assignment(assignment)
                .studentId(studentId)
                .submittedAt(LocalDateTime.now())
                .totalScore(null) // Chưa có điểm
                .build();

        // 5
        List<SubmissionDetail> details = request.getAnswers().stream().map(ans -> {
            Question question = assignment.getQuestions().stream()
                    .filter(q -> q.getId().equals(ans.getQuestionId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Question not found in this assignment"));

            return SubmissionDetail.builder()
                    .submission(submission)
                    .question(question)
                    .studentAnswer(ans.getStudentAnswer())
                    .scoreAwarded(null)
                    .aiFeedback(null)
                    .build();
        }).collect(Collectors.toList());

        submission.setSubmissionDetails(details);

        // 6
        submissionRepository.save(submission);

        // TODO: Send to AI service (Async)
        try {
            List<EssayAnswerDto> essayAnswers = submission.getSubmissionDetails().stream()
                    .map(detail -> EssayAnswerDto.builder()
                            .question_id(detail.getQuestion().getId())
                            .question_text(detail.getQuestion().getContent())
                            .model_answer(detail.getQuestion().getModelAnswer())
                            .student_answer(detail.getStudentAnswer())
                            .weight(detail.getQuestion().getScore() != null ? detail.getQuestion().getScore() : 100)
                            .build())
                    .collect(Collectors.toList());

            SubmissionMessageDto message = SubmissionMessageDto.builder()
                    .submission_id(submission.getId())
                    .assignment_id(assignment.getId())
                    .student_id(submission.getStudentId())
                    .submitted_at(submission.getSubmittedAt().toString())
                    .essay_answers(essayAnswers)
                    .build();

            rabbitTemplate.convertAndSend(submissionQueue, message);
            System.out.println(">>> Sent submission to AI Service: " + submission.getId());

        } catch (Exception e) {
            System.err.println(">>> Failed to send to RabbitMQ: " + e.getMessage());
        }
        return "Submission received successfully";
    }
    public List<StudentGradeResponse> getStudentGrades(String classroomId, String studentId) {
        // 1. Lấy tất cả bài tập trong lớp (Sử dụng hàm vừa thêm bên Repository)
        List<Assignment> assignments = assignmentRepository.findByClassroomId(classroomId);

        // 2. Duyệt qua từng bài tập để tìm submission tương ứng
        return assignments.stream().map(assignment -> {
            // Tìm bài nộp của học sinh cho bài tập này
            Optional<Submission> submissionOpt = submissionRepository.findByAssignmentIdAndStudentId(assignment.getId(), studentId);

            // Xử lý maxScore: Chuyển Integer sang Double để khớp với DTO
            Double maxScoreVal = assignment.getMaxScore() != null ? assignment.getMaxScore().doubleValue() : 0.0;

            if (submissionOpt.isPresent()) {
                Submission submission = submissionOpt.get();
                return StudentGradeResponse.builder()
                        .assignmentId(assignment.getId())
                        .assignmentTitle(assignment.getTitle())
                        .maxScore(maxScoreVal) // <--- Đã sửa lỗi tại đây
                        .submissionId(submission.getId())
                        .score(submission.getTotalScore())
                        .feedback(submission.getFeedback())
                        .submittedAt(submission.getSubmittedAt())
                        .status(submission.getTotalScore() != null ? "Đã chấm" : "Đã nộp")
                        .build();
            } else {
                // Chưa nộp bài
                boolean isOverdue = assignment.getDueDate() != null && assignment.getDueDate().isBefore(LocalDateTime.now());

                return StudentGradeResponse.builder()
                        .assignmentId(assignment.getId())
                        .assignmentTitle(assignment.getTitle())
                        .maxScore(maxScoreVal) // <--- Đã sửa lỗi tại đây
                        .submissionId(null)
                        .score(null)
                        .feedback(null)
                        .submittedAt(null)
                        .status(isOverdue ? "Quá hạn" : "Chưa nộp")
                        .build();
            }
        }).collect(Collectors.toList());
    }
}