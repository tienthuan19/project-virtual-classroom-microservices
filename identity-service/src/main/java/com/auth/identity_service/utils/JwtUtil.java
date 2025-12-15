package com.auth.identity_service.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Set;

@Component
public class JwtUtil {
    @Value("${app.security.jwtsecret}")
    private String JWT_SECRET;
    
    @Value("${app.security.jwtexpiration}")
    private long JWT_EXPIRATION;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    public String generateToken(String userId, String userName, String email, Set<String> roles) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

    return Jwts.builder()
            .setSubject(userId) 
            .claim("userName", userName) 
            .claim("email", email)       
            .claim("roles", roles)
            //TODO: expired_date: Now + 24 hours
            
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