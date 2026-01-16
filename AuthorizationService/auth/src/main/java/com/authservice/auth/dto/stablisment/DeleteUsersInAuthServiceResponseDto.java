package com.authservice.auth.dto.stablisment;

import java.util.List;

import com.authservice.auth.dto.auth.UserDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteUsersInAuthServiceResponseDto {
    private final List<UserDto> deletedUsers;
}
