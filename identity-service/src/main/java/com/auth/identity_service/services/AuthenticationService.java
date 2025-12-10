package com.auth.identity_service.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.identity_service.dto.request.RegisterRequest;
import com.auth.identity_service.dto.responce.UserResponse;
import com.auth.identity_service.exception.AppException;
import com.auth.identity_service.exception.ErrorCode;
import com.auth.identity_service.models.Role;
import com.auth.identity_service.models.User;
import com.auth.identity_service.repository.RoleRepository;
import com.auth.identity_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private boolean checkEmptyNull(Object data){
        if (data == null) {
            return true;
        }
        if (data instanceof String) {
            return ((String) data).isEmpty();
        }
        if (data instanceof java.util.Collection) {
            return ((java.util.Collection<?>) data).isEmpty();
        }
        return false;
    }

    public UserResponse register(RegisterRequest request){
        User registerUser = new User();
        Set<Role> roles = new HashSet<>();

        if(checkEmptyNull(request.getEmail())){
            throw new AppException(ErrorCode.MISSING_INPUT);
        }
        if (userRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if(checkEmptyNull(request.getPassword())){
            throw new AppException(ErrorCode.MISSING_INPUT);
        } 
        if(checkEmptyNull(request.getUsername())){
            throw new AppException(ErrorCode.MISSING_INPUT);
        } 
        if (checkEmptyNull(request.getRole())){
            Role defaultRole = roleRepository.findByName("STUDENT")
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            roles.add(defaultRole);
        }
        // 1. Email
        registerUser.setEmail(request.getEmail());
        // 2. Password (Hashed)
        registerUser.setPassword(passwordEncoder.encode(request.getPassword()));
        // 3. Username
        registerUser.setUsername(request.getUsername());

        request.getRole().forEach(roleNames -> {
            Role role = roleRepository.findByName(roleNames)
                                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            roles.add(role);
            });

        // 4. Roles
        registerUser.setRoles(roles);

        //SAVE
        userService.createUser(registerUser);

        // Set to show list of registerUser's roles
        Set<String> roleNames = new HashSet<>();
        if (registerUser.getRoles() != null) {
        registerUser.getRoles().forEach(role -> roleNames.add(role.getName()));
        }

        return UserResponse.builder()
                .id(registerUser.getId())
                .username(registerUser.getUsername())
                .email(registerUser.getEmail())
                .roles(roleNames)
                .build();
    }
}