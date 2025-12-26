package com.lms.lms_backend.repository;

import com.lms.lms_backend.models.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, String> {
    boolean existsByClassCode(String classCode);
    Optional<Classroom> findByClassCode(String classCode);
}
