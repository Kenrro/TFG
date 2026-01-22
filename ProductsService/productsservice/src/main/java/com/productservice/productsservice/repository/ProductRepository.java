package com.productservice.productsservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.productservice.productsservice.entity.Product;

import jakarta.transaction.Transactional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.stablishmentCode= :stablishmentCode")
    List<Product> findAllByStablishmentCode(@Param("stablishmentCode") String stablishmentCode);

    @Transactional
    @Modifying
    @Query("DELETE FROM Product p WHERE p.stablishmentCode = :code")
    int deleteAllByStablishmentCode(@Param("code") String stablishmentCode);

}
