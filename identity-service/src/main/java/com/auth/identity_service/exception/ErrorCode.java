package com.auth.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // DB conection failures, 
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    // Register
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST), // 400
    // Lookup / Update / Delete
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND), //404
    // Missing or Invalid Token
    UNAUTHENTICATED(1003, "Unauthenticated", HttpStatus.UNAUTHORIZED), // 401
    // Try something that user is not allowed
    UNAUTHORIZED(1004, "You do not have permission", HttpStatus.FORBIDDEN), // 403
    // Missing Input
    MISSING_INPUT(1005, "Missing required input field", HttpStatus.BAD_REQUEST), // 400
    // Register/ dosomething that need ROLE infor
    ROLE_NOT_FOUND(1006, "Role not found", HttpStatus.NOT_FOUND), // 404
    // Login
    INVALID_CREDENTIALS(1007, "Email or password incorrect", HttpStatus.UNAUTHORIZED); // 401

    private int code;
    private String message;
    private HttpStatus statusCode; 

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}