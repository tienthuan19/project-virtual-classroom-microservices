package com.lms.lms_backend.repository;

import com.lms.lms_backend.models.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, String> {
    boolean existsByAssignmentIdAndStudentId(String assignmentId, String studentId);

    // 1. Đếm tổng số bài nộp (Submission) cho các bài tập của giáo viên này
    // Đường đi: Submission -> Assignment -> Classroom -> Check Teacher
    @Query("SELECT COUNT(s) FROM Submission s " +
            "JOIN s.assignment a " +
            "JOIN a.classroom c " +
            "WHERE c.creatorId = :teacherId")
    long countSubmissionsByTeacherId(String teacherId);

    // 2. Tính điểm trung bình (AVG) của toàn bộ bài nộp
    // Sử dụng trường 'totalScore' từ Entity Submission bạn cung cấp
    @Query("SELECT AVG(s.totalScore) FROM Submission s " +
            "JOIN s.assignment a " +
            "JOIN a.classroom c " +
            "WHERE c.creatorId = :teacherId")
    Double findAverageScoreByTeacherId(String teacherId);

    Optional<Submission> findByAssignmentIdAndStudentId(String assignmentId, String studentId);
    void deleteByAssignmentId(String assignmentId);
}
