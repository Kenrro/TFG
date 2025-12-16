package com.authservice.auth.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtFilter {
    @Autowired
    private UserDetailService UserDetailService;
    
}
