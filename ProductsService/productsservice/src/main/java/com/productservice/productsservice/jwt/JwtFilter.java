package com.productservice.productsservice.jwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.Claims;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
    @Autowired
    private JwtUtil jwtUtil;
    
    private static final String BEARER = "Bearer ";

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/h2-console") 
            || path.startsWith("/swagger-ui") 
            || path.startsWith("/v3/api-docs") 
            || path.startsWith("/products");
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                    filterChain.doFilter(request, response);
                    return;
                }
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith(BEARER)) {
                    String token = authHeader.substring(BEARER.length());
                    if (jwtUtil.isTokenValid(token)) {
                        Claims claims = jwtUtil.getClaims(token); // obtiene todos los claims
                        String username = claims.getSubject();
                        String role = claims.get("role", String.class); // ej: "ROLE_SERVICE"

                        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(username, null, authorities);

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
                filterChain.doFilter(request, response);
            }
}
