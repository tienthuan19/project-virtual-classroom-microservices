package com.lms.lms_backend.controllers;

import com.lms.lms_backend.dto.request.ClassroomRequest;
import com.lms.lms_backend.dto.request.JoinClassRequest;
import com.lms.lms_backend.dto.response.*;
import com.lms.lms_backend.services.ClassroomService;
import com.lms.lms_backend.services.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lms-backend/v1")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomService classroomService;
    private final SubmissionService submissionService;

    @PostMapping("/classrooms")
    @PreAuthorize("hasRole('TEACHER') or hasAuthority('crt_cls')")
    public ApiResponse<ClassroomResponse> createClassroom(@RequestBody ClassroomRequest classroomRequest) {
        ClassroomResponse response = classroomService.createClassroom(classroomRequest);
        return ApiResponse.<ClassroomResponse>builder()
                .status(200)
                .data(response)
                .build();
    }
    @GetMapping("/classrooms/teacher/my-classes")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<List<ClassroomCardResponse>> getMyClassrooms() {
        List<ClassroomCardResponse> response = classroomService.getTeacherClassrooms();
        return ApiResponse.<List<ClassroomCardResponse>>builder()
                .status(200)
                .message("Get classroom list successfully")
                .data(response)
                .build();
    }

    @GetMapping("/classrooms/teacher/dashboard-stats")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<TeacherDashboardResponse> getDashboardStats() {
        TeacherDashboardResponse response = classroomService.getTeacherDashboardStats();
        return ApiResponse.<TeacherDashboardResponse>builder()
                .status(200)
                .message("Get dashboard stats successfully")
                .data(response)
                .build();
    }

    @PostMapping("/classrooms/join")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<String> joinClass(@RequestBody JoinClassRequest request) {

        String classroomId = classroomService.joinClassroom(request);

        return ApiResponse.<String>builder()
                .status(200)
                .message("Joined class successfully")
                .data(classroomId)
                .build();
    }

    @GetMapping("/classrooms/student/my-classes")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<List<ClassroomCardResponse>> getStudentClassrooms() {
        List<ClassroomCardResponse> response = classroomService.getStudentClassrooms();

        return ApiResponse.<List<ClassroomCardResponse>>builder()
                .status(200)
                .message("Get joined classrooms successfully")
                .data(response)
                .build();
    }

    @GetMapping("/classrooms/{classroomId}/members")
    @PreAuthorize("hasAnyRole('TEACHER', 'STUDENT')")
    public ApiResponse<List<ClassMemberResponse>> getClassMembers(@PathVariable String classroomId) {
        return ApiResponse.<List<ClassMemberResponse>>builder()
                .status(200)
                .message("Get class members successfully")
                .data(classroomService.getClassMembers(classroomId))
                .build();
    }

    @GetMapping("student/grades/{classroomId}")
    public ApiResponse<List<StudentGradeResponse>> getMyGrades(
            @PathVariable String classroomId
    ) {
        // Lấy userId từ Security Context do Filter đã set
        String studentId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ApiResponse.<List<StudentGradeResponse>>builder()
                .status(200)
                .message("Get grades successfully")
                .data(submissionService.getStudentGrades(classroomId, studentId))
                .build();
    }
}