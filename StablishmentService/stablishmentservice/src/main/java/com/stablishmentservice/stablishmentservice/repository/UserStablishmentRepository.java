package com.stablishmentservice.stablishmentservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.stablishmentservice.stablishmentservice.entity.UserStablishment;

import jakarta.transaction.Transactional;

@Repository
public interface UserStablishmentRepository extends JpaRepository<UserStablishment, Long> {

    @Query("SELECT us.userId FROM UserStablishment us WHERE us.stablishmentId = :stablishmentId")
    List<Long> findAllUsersIdByStablishmentId(@Param("stablishmentId") Long stablishmentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserStablishment u WHERE u.stablishmentId IN :stablishmentId")
    void deleteAllByStablishmentId(@Param("stablishmentId") Long stablishmentId);


    @Query("SELECT us FROM UserStablishment us WHERE us.userId = :userId")
    Optional<UserStablishment> findByEmployeeId(@Param("userId") Long userId);
    @Query("SELECT us FROM UserStablishment us WHERE us.userId = :userId")
    List<UserStablishment> findByUserId(@Param("userId") Long userId);

    @Query("SELECT us FROM UserStablishment us WHERE us.stablishmentId = :stablishmentId")
    List<UserStablishment> findByStablishmentId(@Param("stablishmentId") Long stablishmentId);

    @Modifying
    @Query("DELETE FROM UserStablishment us WHERE us.userId = :userId")
    int deleteByUserId(@Param("userId") Long userId);
    @Modifying
    @Query("DELETE FROM UserStablishment us WHERE us.stablishmentId = :stablishmentId")
    int deleteByStablishmentId(@Param("stablishmentId") Long stablishmentId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserStablishment us WHERE us.stablishmentId = :stablishmentId AND us.userId = :userId")
    int deleteByUsertIdAndStablishmentId(@Param("userId") Long userId, @Param("stablishmentId") Long stablishmentId);
    
} 