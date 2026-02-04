package com.lms.lms_backend.repository;

import com.lms.lms_backend.models.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {
    boolean existsByAssignmentIdAndStudentId(String assignmentId, String studentId);

    @Query("SELECT COUNT(s) FROM Submission s " +
            "JOIN s.assignment a " +
            "JOIN a.classroom c " +
            "WHERE c.creatorId = :teacherId")
    long countSubmissionsByTeacherId(String teacherId);

    @Query("SELECT AVG(s.totalScore) FROM Submission s " +
            "JOIN s.assignment a " +
            "JOIN a.classroom c " +
            "WHERE c.creatorId = :teacherId")
    Double findAverageScoreByTeacherId(String teacherId);

    Optional<Submission> findByAssignmentIdAndStudentId(String assignmentId, String studentId);
    void deleteByAssignmentId(String assignmentId);
}
