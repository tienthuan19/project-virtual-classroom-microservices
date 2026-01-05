package com.auth.identity_service.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.auth.identity_service.dto.responce.UserResponse;
import com.auth.identity_service.models.Permission;
import org.springframework.stereotype.Service;

import com.auth.identity_service.exception.AppException;
import com.auth.identity_service.exception.ErrorCode;
import com.auth.identity_service.models.Role;
import com.auth.identity_service.models.User;
import com.auth.identity_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void createUser(User user) {
        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // getName() of the Set<Role> and map to the new Set<String>
    public Set<String> transferUserRolesToSetOfString(User user) {
        if (user.getRoles() == null) {
            return Set.of();
        }
        return user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());
    }

    public Set<String> transferUserPermissionsToSetOfString(User user) {
        if (user.getRoles() == null) {
            return Set.of();
        }
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<UserResponse> getUsersByIds(List<String> userIds) {
        return userRepository.findAllById(userIds).stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .username(user.getUsername())
                        .build())
                .collect(Collectors.toList());
    }
}
