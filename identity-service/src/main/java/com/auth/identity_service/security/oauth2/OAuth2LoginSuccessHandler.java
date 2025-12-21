package com.auth.identity_service.security.oauth2;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.auth.identity_service.models.User;
import com.auth.identity_service.security.oauth2.user.OAuth2UserInfo;
import com.auth.identity_service.security.oauth2.user.OAuth2UserInfoFactory;
import com.auth.identity_service.services.UserService;
import com.auth.identity_service.utils.JwtUtil;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException, java.io.IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        String registrationId = authToken.getAuthorizedClientRegistrationId();

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());
        
        String email = userInfo.getEmail();
        String name = userInfo.getName();
        String oauth2UserId = userInfo.getId();
        String oauth2ProviderName = registrationId.toUpperCase();
        Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            // User already have account -> GENERATE TOKEN AND REDIRECT
            User user = userOptional.get();
            Set<String> roles = userService.transferUserRolesToSetOfString(user);

            String token = jwtUtil.generateToken(user);
            response.sendRedirect(frontendUrl + "/oauth2/redirect?token=" + token);

        } else {
            // User is new -> REDIRECT TO ROLE SELECTION PAGE
            // TODO: Write a function to create TEMP TOKEN with limited validity
            String tempToken = jwtUtil.generateTempToken(
                "TEMP_ID",
                name, 
                email, 
                oauth2ProviderName,
                oauth2UserId,
                new HashSet<>() 
            );
            response.sendRedirect(frontendUrl + "/register-role?tempToken=" + tempToken);
        }
    }
}
