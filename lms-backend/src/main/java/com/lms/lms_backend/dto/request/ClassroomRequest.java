package com.lms.lms_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomRequest {
    private String code;
    private String name;
    private String subject;
    private String description;
    private String creatorId;
}
