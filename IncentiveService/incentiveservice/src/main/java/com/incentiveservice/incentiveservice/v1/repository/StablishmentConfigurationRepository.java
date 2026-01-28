package com.incentiveservice.incentiveservice.v1.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.incentiveservice.incentiveservice.v1.entity.StablishmentConfiguration;

@Repository
public interface StablishmentConfigurationRepository extends JpaRepository<StablishmentConfiguration, Long> {

        @Query("SELECT sc FROM StablishmentConfiguration sc WHERE sc.stablishmentCode = :stablishmentCode")
        Optional<StablishmentConfiguration> findByStablishmentCode(@Param("stablishmentCode") String stablishmentCode);

}
