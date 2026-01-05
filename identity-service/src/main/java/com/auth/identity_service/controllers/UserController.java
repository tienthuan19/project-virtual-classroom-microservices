package com.auth.identity_service.controllers;

import com.auth.identity_service.dto.responce.ApiResponse;
import com.auth.identity_service.dto.responce.UserResponse;
import com.auth.identity_service.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.auth.identity_service.services.UserService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping(value = "/api/identity-service/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    // Internal API
    @PostMapping("/bulk-info")
    public ApiResponse<List<UserResponse>> getUsersByIds(@RequestBody List<String> userIds) {
        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getUsersByIds(userIds))
                .build();
    }
}