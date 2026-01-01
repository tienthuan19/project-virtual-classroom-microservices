package com.lms.lms_backend.repository;

import com.lms.lms_backend.models.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {
    boolean existsByAssignmentIdAndStudentId(String assignmentId, String studentId);
}
