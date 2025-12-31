package com.lms.lms_backend.services;

import com.lms.lms_backend.dto.response.NotificationResponse;
import com.lms.lms_backend.models.Notification;
import com.lms.lms_backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> getMyNotifications() {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Notification> notifications = notificationRepository.findByRecipientIdOrderByCreatedAtDesc(currentUserId);

        return notifications.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public void markAsRead(String notificationId) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getRecipientId().equals(currentUserId)) {
            throw new RuntimeException("Unauthorized access to this notification");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public long countUnread() {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        return notificationRepository.countByRecipientIdAndIsReadFalse(currentUserId);
    }

    private NotificationResponse mapToResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .message(n.getMessage())
                .isRead(n.isRead())
                .relatedEntityId(n.getRelatedEntityId())
                .type(n.getType())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
