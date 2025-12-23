package com.lms.lms_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_code", referencedColumnName = "class_code")
    private Classroom classroom;

    @Column(name = "sender_id")
    private String senderId; // ID của người gửi (User ID)

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;
}