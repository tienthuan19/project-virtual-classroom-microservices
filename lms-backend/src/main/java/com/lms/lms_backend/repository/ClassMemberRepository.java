package com.lms.lms_backend.repository;

import com.lms.lms_backend.models.ClassMember;
import com.lms.lms_backend.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassMemberRepository extends JpaRepository<ClassMember, Long> {
    boolean existsByUserIdAndClassroom(String userId, Classroom classroom);
    @Query("SELECT COUNT(cm) FROM ClassMember cm " +
            "JOIN cm.classroom c " +
            "WHERE c.creatorId = :creatorId")
    long countTotalStudentsByTeacher(@Param("creatorId") String creatorId);
}
