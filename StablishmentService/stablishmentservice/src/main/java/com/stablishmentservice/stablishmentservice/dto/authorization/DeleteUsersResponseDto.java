package com.stablishmentservice.stablishmentservice.dto.authorization;

import java.util.List;

import lombok.Data;

@Data
public class DeleteUsersResponseDto {
    private List<AuthUserDto> deletedUsers;
}
