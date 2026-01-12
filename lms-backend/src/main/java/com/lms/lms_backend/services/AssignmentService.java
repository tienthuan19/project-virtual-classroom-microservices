package com.lms.lms_backend.services;

import com.lms.lms_backend.dto.request.CreateAssignmentRequest;
import com.lms.lms_backend.dto.request.QuestionRequest;
import com.lms.lms_backend.dto.response.AssignmentDetailResponse;
import com.lms.lms_backend.dto.response.AssignmentResponse;
import com.lms.lms_backend.dto.response.QuestionResponse;
import com.lms.lms_backend.models.Assignment;
import com.lms.lms_backend.models.Classroom;
import com.lms.lms_backend.models.Question;
import com.lms.lms_backend.repository.AssignmentRepository;
import com.lms.lms_backend.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ClassroomRepository classroomRepository;

    @Transactional
    public AssignmentResponse createAssignment(String classroomId, CreateAssignmentRequest request) {
        // 1
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2
        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        // 3
        if (!classroom.getCreatorId().equals(currentUserId)) {
            throw new RuntimeException("You are not allowed to create assignment in this class");
        }
        // ==================================================================
        // VALIDATION
        // ==================================================================
//        if (request.getQuestions() != null && !request.getQuestions().isEmpty()) {
//
//            int totalQuestionScore = request.getQuestions().stream()
//                    .mapToInt(QuestionRequest::getScore)
//                    .sum();
//
//
//            if (totalQuestionScore != request.getMaxScore()) {
//                throw new RuntimeException(
//                        String.format("Sum score of all question (%d) must equal the max score of the assignment (%d)",
//                                totalQuestionScore, request.getMaxScore())
//                );
//            }
//        } else {
//            if (request.getMaxScore() > 0) {
//                throw new RuntimeException("Max score > 0, at least 1 question");
//            }
//        }
        // ==================================================================

        // 4
        Assignment assignment = Assignment.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .duration(request.getDuration())
                .maxScore(request.getMaxScore())
                .classroom(classroom)
                .build();

        // 5
        if (request.getQuestions() != null) {
            List<Question> questions = request.getQuestions().stream().map(qReq -> {
                return Question.builder()
                        .content(qReq.getContent())
                        .modelAnswer(qReq.getModelAnswer())
                        .score(qReq.getScore())
                        .assignment(assignment)
                        .build();
            }).collect(Collectors.toList());

            assignment.setQuestions(questions);
        }

        // 6
        Assignment savedAssignment = assignmentRepository.save(assignment);

        // 7
        return mapToResponse(savedAssignment);
    }

    public List<AssignmentResponse> getAssignmentsByClassroom(String classroomId) {
        //TODO: Check if that student is a member of class
        return assignmentRepository.findByClassroomIdOrderByCreatedAtDesc(classroomId)
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private AssignmentResponse mapToResponse(Assignment a) {
        return AssignmentResponse.builder()
                .id(a.getId())
                .title(a.getTitle())
                .description(a.getDescription())
                .dueDate(a.getDueDate())
                .duration(a.getDuration())
                .maxScore(a.getMaxScore())
                .createdAt(a.getCreatedAt())
                .numberOfQuestions(a.getQuestions() != null ? a.getQuestions().size() : 0)
                .build();
    }
    public AssignmentDetailResponse getAssignmentDetail(String assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        List<QuestionResponse> questionResponses = assignment.getQuestions().stream()
                .map(q -> QuestionResponse.builder()
                        .id(q.getId())
                        .content(q.getContent())
                        .score(q.getScore())
                        .build())
                .collect(Collectors.toList());

        return AssignmentDetailResponse.builder()
                .id(assignment.getId())
                .title(assignment.getTitle())
                .description(assignment.getDescription())
                .dueDate(assignment.getDueDate())
                .duration(assignment.getDuration())
                .maxScore(assignment.getMaxScore())
                .questions(questionResponses)
                .build();
    }

    public List<AssignmentResponse> getPendingAssignmentsForStudent(String classId) {
        String studentId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Assignment> assignments = assignmentRepository.findPendingAssignmentsByClassIdAndStudentId(classId, studentId);

        return assignments.stream()
                .map(this::toAssignmentResponse)
                .toList();
    }

    private AssignmentResponse toAssignmentResponse(Assignment assignment) {
        return AssignmentResponse.builder()
                .id(assignment.getId())
                .title(assignment.getTitle())
                .description(assignment.getDescription())
                .dueDate(assignment.getDueDate())
                .duration(assignment.getDuration())
                .maxScore(assignment.getMaxScore())
                .numberOfQuestions(assignment.getQuestions() != null ? assignment.getQuestions().size() : 0)
                .build();
    }
}
