package com.authservice.auth.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthCreateEstablishMentAdminResponseDto {
    private String adminId;
}
