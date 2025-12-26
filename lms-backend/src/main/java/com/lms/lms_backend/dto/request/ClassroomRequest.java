package com.lms.lms_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomRequest {
    @NotBlank(message = "Class code is required")
    private String code;
    @NotBlank(message = "Class name is required")
    private String name;
    @NotBlank(message = "Class subject is required")
    private String subject;
    private String description;
}
