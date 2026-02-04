package com.lms.lms_backend.controllers;

import com.lms.lms_backend.dto.request.CreateAssignmentRequest;
import com.lms.lms_backend.dto.request.SubmissionRequest;
import com.lms.lms_backend.dto.response.ApiResponse;
import com.lms.lms_backend.dto.response.AssignmentDetailResponse;
import com.lms.lms_backend.dto.response.AssignmentResponse;
import com.lms.lms_backend.services.AssignmentService;
import com.lms.lms_backend.services.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lms-backend/v1/classrooms")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final SubmissionService submissionService;
    // 1
    @PostMapping("/{classroomId}/assignments")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<AssignmentResponse> createAssignment(
            @PathVariable String classroomId,
            @RequestBody CreateAssignmentRequest request) {

        AssignmentResponse result = assignmentService.createAssignment(classroomId, request);

        return ApiResponse.<AssignmentResponse>builder()
                .status(201)
                .message("Assignment created successfully")
                .data(result)
                .build();
    }
    // 2
    @GetMapping("/{classroomId}/assignments")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ApiResponse<List<AssignmentResponse>> getAssignments(@PathVariable String classroomId) {
        List<AssignmentResponse> result = assignmentService.getAssignmentsByClassroom(classroomId);

        return ApiResponse.<List<AssignmentResponse>>builder()
                .status(200)
                .message("Get assignments successfully")
                .data(result)
                .build();
    }
    @GetMapping("/{classroomId}/assignments/pending")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<List<AssignmentResponse>> getPendingAssignments(@PathVariable String classroomId) {
        return ApiResponse.<List<AssignmentResponse>>builder()
                .data(assignmentService.getPendingAssignmentsForStudent(classroomId))
                .build();
    }
    // 3
    @GetMapping("/assignments/{assignmentId}")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ApiResponse<AssignmentDetailResponse> getAssignmentDetail(@PathVariable String assignmentId) {
        AssignmentDetailResponse result = assignmentService.getAssignmentDetail(assignmentId);
        return ApiResponse.<AssignmentDetailResponse>builder()
                .status(200)
                .message("Get assignment detail successfully")
                .data(result)
                .build();
    }
    // 4
    @PostMapping("/{assignmentId}/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<String> submitAssignment(
            @PathVariable String assignmentId,
            @RequestBody SubmissionRequest request) {

        String message = submissionService.submitAssignment(assignmentId, request);

        return ApiResponse.<String>builder()
                .status(200)
                .message(message)
                .build();
    }
    @DeleteMapping("/assignments/{assignmentId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<Void> deleteAssignment(@PathVariable String assignmentId) {
        assignmentService.deleteAssignment(assignmentId);
        return ApiResponse.<Void>builder()
                .status(200)
                .message("Assignment deleted successfully")
                .build();
    }
}