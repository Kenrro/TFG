package com.stablishmentservice.stablishmentservice.dto.stablishment;

import java.time.Instant;

import com.stablishmentservice.stablishmentservice.dto.authorization.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RelationUserStablishmentDto {
    private UserDto userId;
    private Instant registeredAt;
    private int wallet;
}
