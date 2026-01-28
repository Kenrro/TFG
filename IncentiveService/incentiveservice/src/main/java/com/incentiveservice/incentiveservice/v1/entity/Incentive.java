package com.incentiveservice.incentiveservice.v1.entity;

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
@Table(name = "incentives",
    uniqueConstraints = @UniqueConstraint(columnNames = {"productId"})
)
public class Incentive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del producto (Product Service)
    @Column(nullable = false)
    private Long productId;

    // ID del establecimiento (Establishment Service)
    @Column(nullable = false)
    private String stablishmentCode;

    // Puntos necesarios para canjear el incentivo
    @Column(nullable = false)
    private int pointsRequired;

    @Column(nullable = false)
    private boolean active = true;
}
