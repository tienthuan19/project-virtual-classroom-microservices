package com.lms.lms_backend.repository;

import com.lms.lms_backend.models.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, String> {
    List<Assignment> findByClassroomIdOrderByCreatedAtDesc(String classroomId);
    long countByClassroomId(String classroomId);

    @Query("SELECT COUNT(a) FROM Assignment a WHERE a.classroom.id = :classId " +
            "AND a.id NOT IN (SELECT s.assignment.id FROM Submission s WHERE s.studentId = :studentId)")
    long countPendingAssignments(String classId, String studentId);

    @Query("SELECT a FROM Assignment a WHERE a.classroom.id = :classId " +
            "AND a.id NOT IN (SELECT s.assignment.id FROM Submission s WHERE s.studentId = :studentId)")
    List<Assignment> findPendingAssignmentsByClassIdAndStudentId(
            @Param("classId") String classId,
            @Param("studentId") String studentId
    );

    @Query("SELECT COUNT(a) FROM Assignment a " +
            "JOIN a.classroom c " +
            "WHERE c.creatorId = :teacherId")
    long countAssignmentsByTeacherId(String teacherId);

    @Query("SELECT COUNT(m) FROM Assignment a " +
            "JOIN a.classroom c " +             // Join bài tập với lớp học
            "JOIN ClassMember m ON m.classroom = c " + // Join lớp học với thành viên
            "WHERE c.creatorId = :teacherId " +
            "AND m.role = 'STUDENT'")           // Chỉ đếm học sinh, không đếm giáo viên/trợ giảng
    long countTotalExpectedSubmissionsByTeacherId(String teacherId);
}
