package com.stablishmentservice.stablishmentservice.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

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

@Entity
@Table(name = "user_stablishments",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "stablishmentId"})
    }
)
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStablishment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private Long stablishmentId;
    @CreationTimestamp
    private Instant registeredAt;

}
