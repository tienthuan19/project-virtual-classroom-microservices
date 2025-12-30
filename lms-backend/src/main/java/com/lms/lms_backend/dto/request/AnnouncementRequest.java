package com.lms.lms_backend.dto.request;

import com.lms.lms_backend.enums.Priority;
import lombok.Data;

@Data
public class AnnouncementRequest {
    private String title;
    private String content;
    private Priority priority;
    private String attachmentUrl;
    private boolean sendEmail;
}
