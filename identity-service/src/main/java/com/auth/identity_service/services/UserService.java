package com.auth.identity_service.services;

import org.springframework.stereotype.Service;

import com.auth.identity_service.dto.request.RegisterRequest;
import com.auth.identity_service.dto.responce.UserResponse;
import com.auth.identity_service.models.User;
import com.auth.identity_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponse createUser(RegisterRequest request) {   
        //checkValidateRegisterRequest(request);
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        userRepository.save(user);
        System.out.println("User registered: " + user.getUsername());
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    // private void checkValidateRegisterRequest(RegisterRequest request) {
    //     if (request.getUsername() == null || request.getUsername().isEmpty()) {
    //         throw new RuntimeException("Username cannot be empty");
    //     }

    //     if (userRepository.existsByUsername(request.getUsername())) {
    //         throw new RuntimeException("Username already exists, please choose a different one");
    //     }

    //     if (userRepository.existsByEmail(request.getEmail())) {
    //         throw new RuntimeException("Email already registered");
    //     }
    // }
}
