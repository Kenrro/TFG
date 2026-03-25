package com.incentiveservice.incentiveservice.v1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.incentiveservice.incentiveservice.v1.entity.UserPoints;

@Repository
public interface UserPointsRepository extends JpaRepository<UserPoints, Long> {

    @Query("""
        SELECT us
        FROM UserPoints us
        WHERE us.userId = :userId
          AND us.stablishmentCode = :stablishmentCode
    """)
    Optional<UserPoints> findByUserIdAndCode(
        @Param("userId") Long userId,
        @Param("stablishmentCode") String stablishmentCode
    );

    @Query("""
        SELECT us
        FROM UserPoints us
        WHERE us.userId = :userId
    """)
    List<UserPoints> findAllByUserId(
        @Param("userId") Long userId
    );

    @Query("""
        SELECT us
        FROM UserPoints us
        WHERE us.stablishmentCode = :stablishmentCode
    """)
    List<UserPoints> findAllByStablishmentCode(
        @Param("stablishmentCode") String stablishmentCode
    );

    @Query("""
        SELECT us
        FROM UserPoints us
        WHERE us.userId IN :ids AND
        us.stablishmentCode = :stablishmentCode
    """)
    List<UserPoints> findAllByUsersId(
        @Param("ids")List<Long> ids,
        @Param("stablishmentCode") String stablishmentCode
    );
}
