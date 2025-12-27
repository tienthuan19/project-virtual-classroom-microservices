package com.lms.lms_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherDashboardResponse {
    private long totalClassrooms;
    private long totalStudents;
}
