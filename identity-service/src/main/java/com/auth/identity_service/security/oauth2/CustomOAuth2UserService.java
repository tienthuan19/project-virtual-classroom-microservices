package com.auth.identity_service.security.oauth2;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.auth.identity_service.exception.AppException;
import com.auth.identity_service.exception.ErrorCode;
import com.auth.identity_service.security.oauth2.user.FacebookOAuth2UserInfo;
import com.auth.identity_service.security.oauth2.user.GoogleOAuth2UserInfo;
import com.auth.identity_service.security.oauth2.user.OAuth2UserInfo;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if ("google".equalsIgnoreCase(registrationId)) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if ("facebook".equalsIgnoreCase(registrationId)) {
            return new FacebookOAuth2UserInfo(attributes);
        } else {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION); 
        }
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        
        OAuth2UserInfo userInfo = getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());
        
        String providerId = userInfo.getId();
        String email = userInfo.getEmail();
        
        return oAuth2User;
    }
}

