package com.auth.identity_service.controllers;

import com.auth.identity_service.dto.request.AuthRequest;
import com.auth.identity_service.dto.request.RegisterRequest;
import com.auth.identity_service.dto.responce.ApiResponse;
import com.auth.identity_service.dto.responce.AuthResponse;
import com.auth.identity_service.dto.responce.UserResponse;
import com.auth.identity_service.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/identity-service/v1/auth")
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody RegisterRequest request) {
        UserResponse result = authenticationService.register(request);
        
        return ApiResponse.<UserResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> authentication(@RequestBody AuthRequest request) {
        AuthResponse result = authenticationService.authentication(request);
        
        return ApiResponse.<AuthResponse>builder()
                .data(result)
                .build();
    }
    
}
