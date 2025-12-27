package com.lms.lms_backend.services;

import com.lms.lms_backend.dto.request.ClassroomRequest;
import com.lms.lms_backend.dto.response.ClassroomCardResponse;
import com.lms.lms_backend.dto.response.ClassroomResponse;
import com.lms.lms_backend.dto.response.TeacherDashboardResponse;
import com.lms.lms_backend.models.Classroom;
import com.lms.lms_backend.repository.ClassMemberRepository;
import com.lms.lms_backend.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final ClassMemberRepository classMemberRepository;

    public ClassroomResponse createClassroom(ClassroomRequest request) {

        var context = SecurityContextHolder.getContext();
        String currentUserId = context.getAuthentication().getName();

        if (classroomRepository.existsByClassCode(request.getCode())) {
            throw new RuntimeException("Classroom with code " + request.getCode() + " already exists");
        }

        Classroom newClassroom = Classroom.builder()
                .name(request.getName())
                .classCode(request.getCode())
                .description(request.getDescription())
                .subject(request.getSubject())
                .creatorId(currentUserId)
                .build();

        Classroom savedClass = classroomRepository.save(newClassroom);

        return ClassroomResponse.builder()
                .id(savedClass.getId())
                .name(savedClass.getName())
                .classCode(savedClass.getClassCode())
                .subject(savedClass.getSubject())
                .description(savedClass.getDescription())
                .creatorId(savedClass.getCreatorId())
                .createdAt(savedClass.getCreatedAt())
                .build();
    }
    public List<ClassroomCardResponse> getTeacherClassrooms() {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        return classroomRepository.findClassroomsWithStatsByTeacherId(currentUserId);
    }

    public TeacherDashboardResponse getTeacherDashboardStats() {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        long totalClassrooms = classroomRepository.countByCreatorId(currentUserId);
        long totalStudents = classMemberRepository.countTotalStudentsByTeacher(currentUserId);

        return TeacherDashboardResponse.builder()
                .totalClassrooms(totalClassrooms)
                .totalStudents(totalStudents)
                .build();
    }
}
