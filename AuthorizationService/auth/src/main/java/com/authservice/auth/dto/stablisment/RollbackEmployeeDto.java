package com.authservice.auth.dto.stablisment;

import java.time.Instant;

import com.authservice.auth.entity.Role;

import lombok.Data;

@Data
public class RollbackEmployeeDto {
    private Long id;
    private String name;
    private String username;
    private String lastname;
    private Role role;
    private Instant createdAt;
}
