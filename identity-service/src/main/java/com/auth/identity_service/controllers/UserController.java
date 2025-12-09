package com.auth.identity_service.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.auth.identity_service.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/identity-service/v1/users")
@RequiredArgsConstructor
public class UserController {
    //private final UserService userService; 
}