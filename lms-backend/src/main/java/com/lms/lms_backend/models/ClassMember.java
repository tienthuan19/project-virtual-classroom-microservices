package com.lms.lms_backend.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "class_members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"class_code", "user_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_code", referencedColumnName = "class_code", nullable = false)
    private Classroom classroom;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "is_owner", nullable = false)
    private boolean owner = false;

    @Column(name = "member_role")
    private String role;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
}