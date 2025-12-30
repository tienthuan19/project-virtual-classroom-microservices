package com.lms.lms_backend.models;

import com.lms.lms_backend.enums.Priority; // Import Enum vừa tạo
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "announcements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Announcement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String title; // Mới: Tiêu đề

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private Priority priority; // Mới: Mức độ ưu tiên

    private String attachmentUrl; // Mới: Link tệp đính kèm (PDF, Docx, img...)

    private String senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
}
