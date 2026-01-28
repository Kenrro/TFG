package com.incentiveservice.incentiveservice.v1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stablishment_configuration")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StablishmentConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cuántos puntos gana el cliente por cada euro gastado
    @Column(nullable = false)
    private int pointsPerEuro;

    // Código del establecimiento (NO id, porque viene del establishment-service)
    @Column(nullable = false, 
        unique = true,
        name = "stablishment_code"
    )
    private String stablishmentCode;
}