package com.stablishmentservice.stablishmentservice.dto.authorization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersQuantityResponseDto {
    private int customers;
    private int employees;
}
