package com.lms.lms_backend.services;

import com.lms.lms_backend.dto.request.ClassroomRequest;
import com.lms.lms_backend.dto.response.ClassroomResponse;
import com.lms.lms_backend.models.Classroom;
import com.lms.lms_backend.repository.ClassroomRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

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
}
