package com.auth.identity_service.utils;

import com.auth.identity_service.models.Permission;
import com.auth.identity_service.models.Role;
import com.auth.identity_service.models.User;
import com.auth.identity_service.services.UserService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final UserService userService;
    @Value("${app.security.jwtsecret}")
    private String JWT_SECRET;
    
    @Value("${app.security.jwtexpiration}")
    private long JWT_EXPIRATION;

    public JwtUtil(UserService userService) {
        this.userService = userService;
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

    Set<String> roles = userService.transferUserRolesToSetOfString(user);
    Set<String> permissions = userService.transferUserPermissionsToSetOfString(user);;

        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("userName", user.getUsername())
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .claim("permissions", permissions)

                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateTempToken(String userId, String userName, String email,String oauth2ProviderName, String oauth2UserId, Set<String> roles) {
    Date now = new Date();
    //TODO: Set new expiry date for temp token
    Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

    return Jwts.builder()
            .claim("userID", userId)
            .claim("userName", userName) 
            .claim("email", email)
            .claim("oauth2ProviderName", oauth2ProviderName)
            .claim("oauth2UserId", oauth2UserId)
            .claim("roles", roles)       
            
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS512)
            .compact();
    }
    
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder() 
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmailFromToken(String token) {

        return getAllClaimsFromToken(token).get("email", String.class);
    }

    public String getUserNameFromToken(String token) {
        return getAllClaimsFromToken(token).get("userName", String.class);
    }

    //Change the name to fix get Provider names
    public String getAuthProviderNameFromToken(String token) {
        return getAllClaimsFromToken(token).get("oauth2ProviderName", String.class);
    }

    public String getProviderUserIdFromToken(String token) {
        return getAllClaimsFromToken(token).get("oauth2ProviderName", String.class);
    }

    public String getProviderOAuth2UserIdFromToken(String token) {
        return getAllClaimsFromToken(token).get("oauth2UserId", String.class);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }
}