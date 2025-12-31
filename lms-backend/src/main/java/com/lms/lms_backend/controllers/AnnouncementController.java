package com.lms.lms_backend.controllers;

import com.lms.lms_backend.dto.request.AnnouncementRequest;
import com.lms.lms_backend.dto.response.AnnouncementResponse;
import com.lms.lms_backend.dto.response.ApiResponse;
import com.lms.lms_backend.services.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lms-backend/v1/classrooms")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @PostMapping("/{classroomId}/announcements")
    @PreAuthorize("hasRole('TEACHER')")
    public ApiResponse<AnnouncementResponse> createAnnouncement(
            @PathVariable String classroomId,
            @RequestBody AnnouncementRequest request) {

        AnnouncementResponse result = announcementService.createAnnouncement(classroomId, request);

        return ApiResponse.<AnnouncementResponse>builder()
                .status(201)
                .message("Create Success")
                .data(result)
                .build();
    }

    @GetMapping("/{classroomId}/announcements")
    public ApiResponse<List<AnnouncementResponse>> getAnnouncements(
            @PathVariable String classroomId) {

        List<AnnouncementResponse> result = announcementService.getAnnouncements(classroomId);

        return ApiResponse.<List<AnnouncementResponse>>builder()
                .status(200)
                .message("Get list Success")
                .data(result)
                .build();
    }
}
