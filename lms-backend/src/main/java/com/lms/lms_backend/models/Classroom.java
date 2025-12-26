package com.lms.lms_backend.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "classrooms")
@NoArgsConstructor
@AllArgsConstructor
public class Classroom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Size(max = 10)
    @NotNull
    @Column(name = "class_code", nullable = false, length = 10)
    private String classCode;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "creator_id", length = Integer.MAX_VALUE)
    private String creatorId;
}