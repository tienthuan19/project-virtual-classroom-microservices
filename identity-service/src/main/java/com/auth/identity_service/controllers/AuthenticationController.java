package com.auth.identity_service.controllers;

import com.auth.identity_service.dto.request.RegisterRequest;
import com.auth.identity_service.utils.ApiResponse;
import com.auth.identity_service.dto.responce.UserResponse;
import com.auth.identity_service.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/identity-service/v1/auth") 
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody RegisterRequest request) {
        return ApiResponse.success(authenticationService.register(request));
    }

}
