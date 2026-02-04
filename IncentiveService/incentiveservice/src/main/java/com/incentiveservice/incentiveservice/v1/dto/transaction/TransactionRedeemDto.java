package com.incentiveservice.incentiveservice.v1.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRedeemDto {
    private String stablishmentCode;
    private Long customerId;
    private Long incentiveId;
    
}
