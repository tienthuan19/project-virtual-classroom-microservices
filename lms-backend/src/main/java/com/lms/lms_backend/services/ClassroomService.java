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
        // 1. Lấy ID của User đang đăng nhập từ SecurityContext
        // (Do JwtAuthenticationFilter đã set vào trước đó)
        var context = SecurityContextHolder.getContext();
        String currentUserId = context.getAuthentication().getName();
        // Lưu ý: .getName() sẽ trả về cái mình set ở "Principal" trong Filter.
        // Nếu trong Filter bạn set Principal là userId -> nó ra userId.
        // Nếu set là username -> nó ra username. (Nên set userId để lưu DB cho chuẩn).

        // 2. Check trùng mã lớp
        // Lưu ý: request.getCode() hay getClassCode() phụ thuộc vào cách bạn đặt tên trong DTO
        if (classroomRepository.existsByClassCode(request.getCode())) {
            throw new RuntimeException("Classroom with code " + request.getCode() + " already exists");
        }

        // 3. Map Request -> Entity
        Classroom newClassroom = Classroom.builder()
                .name(request.getName())
                .classCode(request.getCode())
                .description(request.getDescription())
                .subject(request.getSubject())
                .creatorId(currentUserId)
                .build();

        Classroom savedClass = classroomRepository.save(newClassroom);

        // 5. Map Entity -> Response
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
