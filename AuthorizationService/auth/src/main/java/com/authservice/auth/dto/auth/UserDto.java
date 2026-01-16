package com.authservice.auth.dto.auth;

import java.time.Instant;

import com.authservice.auth.entity.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String name;
    private String username;
    private String lastname;
    private Role role;
    private Instant createdAt;
}
