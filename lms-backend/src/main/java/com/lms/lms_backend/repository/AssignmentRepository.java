package com.lms.lms_backend.repository;

import com.lms.lms_backend.models.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, String> {
    List<Assignment> findByClassroomIdOrderByCreatedAtDesc(String classroomId);
}
