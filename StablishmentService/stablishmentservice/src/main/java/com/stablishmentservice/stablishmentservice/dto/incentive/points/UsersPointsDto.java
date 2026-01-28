package com.stablishmentservice.stablishmentservice.dto.incentive.points;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersPointsDto {
    private List<UserPointsDto> userPoints;
}
