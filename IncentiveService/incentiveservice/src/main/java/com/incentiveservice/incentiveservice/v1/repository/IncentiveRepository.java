package com.incentiveservice.incentiveservice.v1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.incentiveservice.incentiveservice.v1.entity.Incentive;

import jakarta.transaction.Transactional;

@Repository
public interface IncentiveRepository extends JpaRepository<Incentive, Long>{

    @Query("SELECT i FROM Incentive i WHERE i.stablishmentCode = :stablishmentCode")
    List<Incentive> findAllByStablishmentCode(@Param("stablishmentCode") String stablishmentCode);

    @Transactional
    @Modifying
    @Query("DELETE FROM Incentive i WHERE i.stablishmentCode = :stablishmentCode")
    void deleteAllByStablishmentCode(@Param("stablishmentCode") String stablihsmentCode);

    @Modifying
    @Transactional
    @Query("DELETE FROM Incentive i WHERE i.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);

}
