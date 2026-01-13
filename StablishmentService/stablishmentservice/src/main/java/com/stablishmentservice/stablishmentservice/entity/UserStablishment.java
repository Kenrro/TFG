package com.stablishmentservice.stablishmentservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;

@Entity
@Table(name = "user_stablishments",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "stablishmentId"})
    }
)
@Builder
public class UserStablishment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long stablishmentId;

}
