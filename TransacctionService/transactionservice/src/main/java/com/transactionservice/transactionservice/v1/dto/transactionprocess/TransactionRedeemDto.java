package com.transactionservice.transactionservice.v1.dto.transactionprocess;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionRedeemDto {
    private String stablishmentCode;
    private Long customerId;
    private Long incentiveId;
    
}
