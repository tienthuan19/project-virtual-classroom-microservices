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
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String recipientId;
    private String message;
    private boolean isRead;

    private String relatedEntityId;
    private String type;
}