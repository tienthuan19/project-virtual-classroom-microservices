package com.lms.lms_backend.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "submission_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(columnDefinition = "TEXT")
    private String studentAnswer;

    @Column(name = "score_awarded")
    private Double scoreAwarded;

    @Column(columnDefinition = "TEXT")
    private String aiFeedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    @ToString.Exclude
    private Submission submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;
}