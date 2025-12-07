package com.auth.identity_service.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.identity_service.dto.request.RegisterRequest;
import com.auth.identity_service.dto.responce.UserResponse;
import com.auth.identity_service.services.UserService;
import com.auth.identity_service.utils.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor 
@RequestMapping(value = "/api/identity-service/v1/users")

public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> Register(@RequestBody RegisterRequest request) {
        try {
            UserResponse userResponse = userService.createUser(request);
            return ApiResponse.success(userResponse);

        } catch (Exception e) {
            return ApiResponse.error(1001, e.getMessage());
        }
        
    }
}
