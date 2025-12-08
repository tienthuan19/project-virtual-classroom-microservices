package com.auth.identity_service.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.identity_service.dto.request.RegisterRequest;
import com.auth.identity_service.dto.responce.UserResponse;
import com.auth.identity_service.models.Role;
import com.auth.identity_service.models.User;
import com.auth.identity_service.repository.RoleRepository;
import com.auth.identity_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public UserResponse createUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("This Email is already in use."); 
        }

        User newUser = new User();
        HashSet<Role> roles = new HashSet<>();
        // 1. Set username
        newUser.setUsername(request.getUsername());
        // 2. Set email
        newUser.setEmail(request.getEmail());
        // 3. Set password (Hashed).
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        String hashedPassword = encoder.encode(request.getPassword());
        System.out.print(hashedPassword);

        newUser.setPassword(hashedPassword);
        if (request.getRole() != null && !request.getRole().isEmpty()) {
            request.getRole().forEach(roleName -> {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roles.add(role);
            });
        } else {
            Role defaultRole = roleRepository.findByName("STUDENT")
                    .orElseThrow(() -> new RuntimeException("Error: Role STUDENT is not found."));
            roles.add(defaultRole);
        }
        // 4. Set roles
        newUser.setRoles(roles); 
        
        userRepository.save(newUser);

        Set<String> roleNames = new HashSet<>();
        if (newUser.getRoles() != null) {
        newUser.getRoles().forEach(role -> roleNames.add(role.getName()));
        }
        return UserResponse.builder()
                .id(newUser.getId())
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .roles(roleNames)
                .build();
    }
}
