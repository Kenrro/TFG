package com.stablishmentservice.stablishmentservice.dto.authorization;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteUsersInAuthServiceRequestDto {
    private final List<Long> userIds;
}
