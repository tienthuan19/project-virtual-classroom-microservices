package com.lms.lms_backend.repository;

import com.lms.lms_backend.dto.response.ClassroomCardResponse;
import com.lms.lms_backend.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, String> {
    boolean existsByClassCode(String classCode);
    Optional<Classroom> findByClassCode(String classCode);
    long countByCreatorId(String creatorId);


    @Query("SELECT new com.lms.lms_backend.dto.response.ClassroomCardResponse(" +
            "c.id, " +
            "c.name, " +
            "c.classCode, " +
            "c.subject, " +
            "c.description, " +
            "c.createdAt, " +
            "(SELECT COUNT(m) FROM ClassMember m WHERE m.classroom = c), " +
            "(SELECT COUNT(a) FROM Assignment a WHERE a.classroom = c), " +
            "0L) " +
            "FROM Classroom c " +
            "WHERE c.creatorId = :creatorId " +
            "ORDER BY c.createdAt DESC")
    List<ClassroomCardResponse> findClassroomsWithStatsByTeacherId(@Param("creatorId") String creatorId);

    @Query("SELECT new com.lms.lms_backend.dto.response.ClassroomCardResponse(" +
            "c.id, " +
            "c.name, " +
            "c.classCode, " +
            "c.subject, " +
            "c.description, " +
            "c.createdAt, " +
            "(SELECT COUNT(m) FROM ClassMember m WHERE m.classroom = c), " +
            "(SELECT COUNT(a) FROM Assignment a WHERE a.classroom = c), " +
            "(SELECT COUNT(a) FROM Assignment a WHERE a.classroom = c " +
            " AND a.id NOT IN (SELECT s.assignment.id FROM Submission s WHERE s.studentId = :studentId))" +
            ") " +
            "FROM Classroom c " +
            "JOIN ClassMember cm ON c.id = cm.classroom.id " +
            "WHERE cm.userId = :studentId " +
            "ORDER BY cm.joinedAt DESC")
    List<ClassroomCardResponse> findClassroomsByStudentId(@Param("studentId") String studentId);
}
