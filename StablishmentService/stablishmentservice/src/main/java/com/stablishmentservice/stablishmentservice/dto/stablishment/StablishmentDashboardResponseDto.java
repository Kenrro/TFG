package com.stablishmentservice.stablishmentservice.dto.stablishment;


import com.stablishmentservice.stablishmentservice.dto.authorization.UsersQuantityResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StablishmentDashboardResponseDto {
    private UsersQuantityResponseDto usersQuantity;
    private TransactionInformationDto transactionInformation;
    private int productsQuantity;
    private int incentiveQuantity;
}
