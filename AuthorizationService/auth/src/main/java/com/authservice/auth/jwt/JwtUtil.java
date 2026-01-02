package com.authservice.auth.jwt;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.authservice.auth.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
    @Value("${app.jwt.secret.private}")
    private String secretStr;
    @Value("${app.jwt.expiration-ms:86400000}")
    private long expirationMs;
    private PrivateKey privateKey;

    @PostConstruct
    private void init() throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(
        secretStr
            .replaceAll("\\s+", "")   // elimina saltos y espacios
    );
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf =KeyFactory.getInstance("RSA");
        this.privateKey = kf.generatePrivate(spec);
        
    }
    public String generateToken(User user) {
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + expirationMs);
        return Jwts.builder()
            .setSubject(user.getUsername())
            .setIssuedAt(now)
            .setExpiration(exp)
            .addClaims(extraClaims(user))
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact();
    }
    public String generateToken(User user, String establishmentCode) {
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + expirationMs);
        Map<String, Object> claims = extraClaims(user);
        claims.put("establishmentCode", establishmentCode);
        return Jwts.builder()
            .setSubject(user.getUsername())
            .setIssuedAt(now)
            .setExpiration(exp)
            .addClaims(claims)
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact();
    }
    private Map<String, Object> extraClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        claims.put("name", user.getName());
        claims.put("lastname", user.getLastname());
        return claims;
    }
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }
    public boolean isTokenValid(String token) {
        try{
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch(JwtException | IllegalAccessError e){
            return false;
        }
    }
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(privateKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
