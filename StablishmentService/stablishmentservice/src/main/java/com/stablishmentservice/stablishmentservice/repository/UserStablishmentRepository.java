package com.stablishmentservice.stablishmentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.stablishmentservice.stablishmentservice.entity.UserStablishment;

@Repository
public interface UserStablishmentRepository extends JpaRepository<UserStablishment, Long> {

    
} 