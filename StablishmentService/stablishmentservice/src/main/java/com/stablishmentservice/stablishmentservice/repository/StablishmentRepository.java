package com.stablishmentservice.stablishmentservice.repository;

import org.springframework.stereotype.Repository;

import com.stablishmentservice.stablishmentservice.entity.Stablishment;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface StablishmentRepository extends JpaRepository<Stablishment, Long> {

}
