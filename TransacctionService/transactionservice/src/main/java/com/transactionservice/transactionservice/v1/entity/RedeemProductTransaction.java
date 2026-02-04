package com.transactionservice.transactionservice.v1.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class RedeemProductTransaction extends Transaction {

    private Integer pointsRequired;
    private Long productId;
    private Long incentiveId;
}
