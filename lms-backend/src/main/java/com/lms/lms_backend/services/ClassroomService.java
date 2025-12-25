package com.lms.lms_backend.services;

import com.lms.lms_backend.dto.request.ClassroomRequest;
import com.lms.lms_backend.models.Classroom;
import com.lms.lms_backend.repository.ClassroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    public Classroom createClassroom(ClassroomRequest request) {
        Classroom requestNewClass = new Classroom();
        requestNewClass.setName(request.getName());
        requestNewClass.setDescription(request.getDescription());
        requestNewClass.setCreatorId(request.getCreatorId());
        requestNewClass.setSubject(request.getSubject());

        //TODO: Add Decode func to decode JWT from client to take UserID
        //requestNewClass.setCreatorId(request.getCreatorId());

        Classroom newClass = classroomRepository.save(requestNewClass);
        return new Classroom();
    }
}
