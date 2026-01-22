package com.productservice.productsservice.entity;


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
@Table(
    name = "products",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"stablishmentCode", "name"})
    }
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String stablishmentCode;
    @Column(length = 100, nullable = false)
    private String name;
    @Column(length = 250, nullable = false)
    private String description;
    @Column(nullable = false)
    private float price;
    @Column(nullable = false)
    @Builder.Default
    private boolean available = true;
}
