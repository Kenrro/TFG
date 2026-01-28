package com.incentiveservice.incentiveservice.v1.entity;

import com.incentiveservice.incentiveservice.v1.enums.UserPointsError;
import com.incentiveservice.incentiveservice.v1.exception.GeneralException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "user_points",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "stablishment_code"})
)
public class UserPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "stablishment_code", nullable = false)
    private String stablishmentCode;

    @Column(nullable = false)
    private int balance;

    public void addPoints(double amount) {
        if (amount <= 0) throw new GeneralException(UserPointsError.INVALID_POINTS_AMOUNT);
        balance += amount;
    }

    public void subtractPoints(double amount) {
        if (amount <= 0) throw new GeneralException(UserPointsError.INVALID_POINTS_AMOUNT);
        if (balance - amount >= 0) {
            balance -= amount;
        } else {
            throw new GeneralException(UserPointsError.INVALID_POINTS_AMOUNT);
        }
    }
}

