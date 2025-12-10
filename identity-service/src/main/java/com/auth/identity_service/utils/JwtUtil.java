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
            
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(getSigningKey(), SignatureAlgorithm.HS512)
            .compact();
    }
    
    // private Claims getAllClaimsFromToken(String token) {
    //     return Jwts.parserBuilder()
    //             .setSigningKey(getSigningKey())
    //             .build()
    //             .parseClaimsJws(token)
    //             .getBody();
    // }

    // public String getEmailFromToken(String token) {
    //     return getAllClaimsFromToken(token).get("email", String.class);
    // }

    // public List<String> getRolesFromToken(String token) {
    //     return getAllClaimsFromToken(token).get("roles", List.class);
    // }

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