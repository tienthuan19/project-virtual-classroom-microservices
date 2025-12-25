package com.lms.lms_backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "classrooms")
public class Classroom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @jakarta.validation.constraints.Size(max = 10)
    @jakarta.validation.constraints.NotNull
    @Column(name = "class_code", nullable = false, length = 10)
    private String classCode;

    @jakarta.validation.constraints.Size(max = 255)
    @jakarta.validation.constraints.NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @jakarta.validation.constraints.NotNull
    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "creator_id", length = Integer.MAX_VALUE)
    private String creatorId;
}