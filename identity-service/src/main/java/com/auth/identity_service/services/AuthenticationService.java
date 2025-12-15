package com.auth.identity_service.services;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.identity_service.dto.request.AuthRequest;
import com.auth.identity_service.dto.request.Oauth2RegisterRequest;
import com.auth.identity_service.dto.request.RegisterRequest;
import com.auth.identity_service.dto.responce.AuthResponse;
import com.auth.identity_service.dto.responce.UserResponse;
import com.auth.identity_service.exception.AppException;
import com.auth.identity_service.exception.ErrorCode;
import com.auth.identity_service.models.Role;
import com.auth.identity_service.models.User;
import com.auth.identity_service.repository.RoleRepository;
import com.auth.identity_service.repository.UserRepository;
import com.auth.identity_service.utils.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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

        if(checkEmptyNull(request.getEmail()) || checkEmptyNull(request.getPassword()) || checkEmptyNull(request.getUsername())){
            throw new AppException(ErrorCode.MISSING_INPUT);
        }

        if (userRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        if (checkEmptyNull(request.getRoles())){
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

        request.getRoles().forEach(roleNames -> {
            Role role = roleRepository.findByName(roleNames)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            roles.add(role);
            });

        // 4. Roles
        registerUser.setRoles(roles);
        // 5. Provider (default: LOCAL)
        registerUser.setOauth2ProviderName("LOCAL");
        // 6. Provider ID (default: LOCAL)
        registerUser.setOauth2UserId("LOCAL");    

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

    public AuthResponse authentication(AuthRequest authenticationRequest){
        if (checkEmptyNull(authenticationRequest.getEmail()) || checkEmptyNull(authenticationRequest.getPassword())){
        throw new AppException(ErrorCode.MISSING_INPUT);
        }

        // *** CHECK AGAIN
        User user = userService.getUserByEmail(authenticationRequest.getEmail());

        boolean isMatched = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());

        if (!isMatched) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }
        user.setLastLogin(LocalDateTime.now());
        Set<String> roles = userService.transferUserRolesToSetOfString(user);

        String token = jwtUtil.generateToken(
                String.valueOf(user.getId()), 
                user.getUsername(),           
                user.getEmail(),              
                roles                         
        );

        Set<String> roleNames = new HashSet<>();
        if (user.getRoles() != null) {
        user.getRoles().forEach(role -> roleNames.add(role.getName()));
        }

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getLastLogin(),
                roleNames
        );


        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    public AuthResponse registerWithOauth2(Oauth2RegisterRequest request) {
        // Validate Token
        if (!jwtUtil.validateToken(request.getTempToken())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        // Get infor from Token
        String email = jwtUtil.getEmailFromToken(request.getTempToken()); 
        String name = jwtUtil.getUserNameFromToken(request.getTempToken());
        String oauth2ProviderName = jwtUtil.getAuthProviderNameFromToken(request.getTempToken());
        String oauth2UserId = jwtUtil.getProviderOAuth2UserIdFromToken(request.getTempToken());
        // Check if email already existed
        if (userService.existsByEmail(email)) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User newUser = new User();
        // 1. Email
        newUser.setEmail(email);
        // 2. Username
        newUser.setUsername(name);
        // 3. Password (default)
        newUser.setPassword("OAUTH2_USER_NO_PASSWORD"); 
        // 4. OAuth2 Provider Name
        newUser.setOauth2ProviderName(oauth2ProviderName);
        // 5. OAuth2 User ID
        newUser.setOauth2UserId(oauth2UserId);
        
        Set<Role> roles = new HashSet<>();
        Role selectedRole = roleRepository.findByName(request.getRole().toUpperCase())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        roles.add(selectedRole);
        // 6. Role
        newUser.setRoles(roles);
        //SAVE
        userService.createUser(newUser);

        String finalToken = jwtUtil.generateToken(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getEmail(),
                Set.of(selectedRole.getName())
        );

        return AuthResponse.builder()
                .token(finalToken)
                .user(UserResponse.builder()
                        .id(newUser.getId())
                        .email(newUser.getEmail())
                        .roles(Set.of(selectedRole.getName()))
                        .build())
                .build();
    }
}
