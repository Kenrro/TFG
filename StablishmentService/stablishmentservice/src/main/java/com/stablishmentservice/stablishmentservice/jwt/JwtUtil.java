package com.stablishmentservice.stablishmentservice.jwt;

import java.security.PublicKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;

@Data
@Component
public class JwtUtil {
    private PublicKey publicKey;
    private String serviceToken;
    
    public String getUserToken(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            String token = (String) auth.getCredentials();
            return token;
        }
        // TODO: Make this 
        throw new RuntimeException("No token");
    }
    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public <T> T getClaim(String token, String claimName, Class<T> clazz) {
        return getClaims(token).get(claimName, clazz);
    }
    public boolean isTokenValid(String token) {
        try {
            Claims claims = getClaims(token);
            return !isTokenExpired(claims);
        } catch (Exception e) {
            return false;
        }
    }
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new java.util.Date());
    }
}
