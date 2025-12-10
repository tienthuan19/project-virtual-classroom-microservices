package com.auth.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    MISSING_INPUT(1005, "Missing required input field", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1006, "Role not found", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1003, "Unauthenticated", HttpStatus.UNAUTHORIZED), // 401
    UNAUTHORIZED(1004, "You do not have permission", HttpStatus.FORBIDDEN); // 403

    private int code;
    private String message;
    private HttpStatus statusCode; 

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}