package com.lms.lms_backend.controllers;

import com.lms.lms_backend.dto.response.ApiResponse;
import com.lms.lms_backend.dto.response.NotificationResponse;
import com.lms.lms_backend.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lms-backend/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ApiResponse<List<NotificationResponse>> getMyNotifications() {
        return ApiResponse.<List<NotificationResponse>>builder()
                .status(200)
                .message("Success")
                .data(notificationService.getMyNotifications())
                .build();
    }

    // PUT /api/v1/notifications/{id}/read
    @PutMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(@PathVariable String id) {
        notificationService.markAsRead(id);
        return ApiResponse.<Void>builder()
                .status(200)
                .message("Marked as read")
                .build();
    }

    // GET /api/v1/notifications/unread-count
    @GetMapping("/unread-count")
    public ApiResponse<Long> countUnread() {
        return ApiResponse.<Long>builder()
                .status(200)
                .message("Success")
                .data(notificationService.countUnread())
                .build();
    }
}