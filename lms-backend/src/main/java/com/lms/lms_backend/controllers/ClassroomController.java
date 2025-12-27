package com.lms.lms_backend.controllers;

import com.lms.lms_backend.dto.request.ClassroomRequest;
import com.lms.lms_backend.dto.response.ApiResponse;
import com.lms.lms_backend.dto.response.ClassroomCardResponse;
import com.lms.lms_backend.dto.response.ClassroomResponse;
import com.lms.lms_backend.dto.response.TeacherDashboardResponse;
import com.lms.lms_backend.services.ClassroomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lms-backend/v1")
@RequiredArgsConstructor
public class ClassroomController {
    private final ClassroomService classroomService;

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

}
