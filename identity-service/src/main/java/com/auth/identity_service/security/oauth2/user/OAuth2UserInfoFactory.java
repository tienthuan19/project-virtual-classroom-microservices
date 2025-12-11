package com.auth.identity_service.security.oauth2.user;

import java.util.Map;

import com.auth.identity_service.exception.AppException;
import com.auth.identity_service.exception.ErrorCode;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("google")) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase("facebook")) {
            return new FacebookOAuth2UserInfo(attributes);
        } else {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION); 
        }
    }
}
