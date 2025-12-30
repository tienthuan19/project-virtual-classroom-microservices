package com.lms.lms_backend.dto.response;

import com.lms.lms_backend.enums.Priority;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AnnouncementResponse {
    private String id;
    private String title;
    private String content;
    private Priority priority;
    private String attachmentUrl;
    private String senderId;
    private LocalDateTime createdAt;
}
