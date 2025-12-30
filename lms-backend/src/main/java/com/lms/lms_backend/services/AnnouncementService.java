package com.lms.lms_backend.services;

import com.lms.lms_backend.dto.request.AnnouncementRequest;
import com.lms.lms_backend.dto.response.AnnouncementResponse;
import com.lms.lms_backend.enums.Priority;
import com.lms.lms_backend.models.Announcement;
import com.lms.lms_backend.models.ClassMember;
import com.lms.lms_backend.models.Classroom;
import com.lms.lms_backend.models.Notification;
import com.lms.lms_backend.repository.AnnouncementRepository;
import com.lms.lms_backend.repository.ClassMemberRepository;
import com.lms.lms_backend.repository.ClassroomRepository;
import com.lms.lms_backend.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final ClassroomRepository classroomRepository;
    private final ClassMemberRepository classMemberRepository;
    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    @Transactional
    public AnnouncementResponse createAnnouncement(String classroomId, AnnouncementRequest request) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        Classroom classroom = classroomRepository.findById(classroomId)
                .orElseThrow(() -> new RuntimeException("Classroom not found"));

        if (!classroom.getCreatorId().equals(currentUserId)) {
            throw new RuntimeException("You are not allowed to access this classroom");
        }
        Announcement announcement = Announcement.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .priority(request.getPriority() != null ? request.getPriority() : Priority.NORMAL)
                .attachmentUrl(request.getAttachmentUrl())
                .senderId(currentUserId)
                .classroom(classroom)
                .build();

        Announcement savedAnnouncement = announcementRepository.save(announcement);

        List<ClassMember> members = classMemberRepository.findByClassroomId(classroomId);
        List<Notification> notifications = new ArrayList<>();

        for (ClassMember member : members) {
            if (member.getUserId().equals(currentUserId)) continue;

            Notification noti = Notification.builder()
                    .recipientId(member.getUserId())
                    .message("Thông báo mới: " + request.getTitle())
                    .isRead(false)
                    .relatedEntityId(savedAnnouncement.getId())
                    .type("ANNOUNCEMENT")
                    .build();
            notifications.add(noti);

            if (request.isSendEmail()) {
                // TODO: Bạn cần lấy email thật của user từ Identity Service.
                String studentEmail = member.getUserId() + "@student.school.com";

                String subject = "[LỚP " + classroom.getName() + "] " + request.getTitle();
                String body = "Giáo viên vừa đăng thông báo:\n" + request.getContent() +
                        "\nĐộ ưu tiên: " + savedAnnouncement.getPriority();

                emailService.sendAnnouncementEmail(studentEmail, subject, body);
            }
        }

        notificationRepository.saveAll(notifications);

        return mapToResponse(savedAnnouncement);
    }

    public List<AnnouncementResponse> getAnnouncements(String classroomId) {
        return announcementRepository.findByClassroomIdOrderByCreatedAtDesc(classroomId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AnnouncementResponse mapToResponse(Announcement a) {
        return AnnouncementResponse.builder()
                .id(a.getId())
                .title(a.getTitle())
                .content(a.getContent())
                .priority(a.getPriority())
                .attachmentUrl(a.getAttachmentUrl())
                .senderId(a.getSenderId())
                .createdAt(a.getCreatedAt())
                .build();
    }
}
