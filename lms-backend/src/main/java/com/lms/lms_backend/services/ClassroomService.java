package com.lms.lms_backend.services;

import com.lms.lms_backend.dto.request.ClassroomRequest;
import com.lms.lms_backend.dto.request.JoinClassRequest;
import com.lms.lms_backend.dto.response.ClassroomCardResponse;
import com.lms.lms_backend.dto.response.ClassroomResponse;
import com.lms.lms_backend.dto.response.TeacherDashboardResponse;
import com.lms.lms_backend.models.ClassMember;
import com.lms.lms_backend.models.Classroom;
import com.lms.lms_backend.repository.AssignmentRepository;
import com.lms.lms_backend.repository.ClassMemberRepository;
import com.lms.lms_backend.repository.ClassroomRepository;
import com.lms.lms_backend.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final ClassMemberRepository classMemberRepository;
    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;

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
        long totalAssignments = assignmentRepository.countAssignmentsByTeacherId(currentUserId);
        Double avgScoreObj = submissionRepository.findAverageScoreByTeacherId(currentUserId);
        double averageScore = (avgScoreObj != null) ? avgScoreObj : 0.0;
        long totalSubmissions = submissionRepository.countSubmissionsByTeacherId(currentUserId);
        long expectedSubmissions = assignmentRepository.countTotalExpectedSubmissionsByTeacherId(currentUserId);

        double completionRate = 0.0;
        if (expectedSubmissions > 0) {
            completionRate = (double) totalSubmissions / expectedSubmissions * 100;
        }

        averageScore = Math.round(averageScore * 100.0) / 100.0;
        completionRate = Math.round(completionRate * 100.0) / 100.0;

        return TeacherDashboardResponse.builder()
                .totalClassrooms(totalClassrooms)
                .totalStudents(totalStudents)
                .totalAssignments(totalAssignments)
                .averageScore(averageScore)
                .completionRate(completionRate)
                .build();
    }
    public String joinClassroom(JoinClassRequest request) {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        Classroom classroom = classroomRepository.findByClassCode(request.getClassCode())
                .orElseThrow(() -> new RuntimeException("Class code not found or invalid"));

        if (classMemberRepository.existsByUserIdAndClassroom(currentUserId, classroom)) {
            throw new RuntimeException("You are already a member of this class");
        }

        if (classroom.getCreatorId().equals(currentUserId)) {
            throw new RuntimeException("You are the creator of this class");
        }

        ClassMember newMember = ClassMember.builder()
                .userId(currentUserId)
                .classroom(classroom)
                .owner(false)
                .joinedAt(LocalDateTime.now())
                .role("STUDENT")
                .build();

        classMemberRepository.save(newMember);

        return classroom.getId();
    }
    public List<ClassroomCardResponse> getStudentClassrooms() {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();

        return classroomRepository.findClassroomsByStudentId(currentUserId);
    }
}
