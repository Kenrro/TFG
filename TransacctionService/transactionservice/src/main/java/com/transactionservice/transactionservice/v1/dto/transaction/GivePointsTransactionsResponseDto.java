package com.transactionservice.transactionservice.v1.dto.transaction;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GivePointsTransactionsResponseDto {
    private List<GivePointsDto> transactions;
}
