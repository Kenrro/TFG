package com.stablishmentservice.stablishmentservice.dto.stablishment;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UsersRelationsResponseDto {
    private List<RelationUserStablishmentDto> relations;
}
