package com.transactionservice.transactionservice.v1.entity;

import java.math.BigDecimal;

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
public class GivePointsTransaction extends Transaction {
    private int pointsGiven;
    private BigDecimal amountSpent;
}


