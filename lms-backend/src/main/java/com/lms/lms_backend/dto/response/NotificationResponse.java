package com.lms.lms_backend.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {
    private String id;
    private String message;
    private boolean isRead;
    private String relatedEntityId;
    private String type;
    private LocalDateTime createdAt;
}
