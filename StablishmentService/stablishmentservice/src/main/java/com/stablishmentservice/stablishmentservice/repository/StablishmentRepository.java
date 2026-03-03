package com.stablishmentservice.stablishmentservice.repository;

import org.springframework.stereotype.Repository;

import com.stablishmentservice.stablishmentservice.entity.Stablishment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface StablishmentRepository extends JpaRepository<Stablishment, Long> {
    @Query("SELECT s.code FROM Stablishment s WHERE s.code LIKE CONCAT(:prefix, '%') ORDER BY s.code DESC")
    List<String> findLastCodeByPrefix(@Param("prefix") String prefix);

    boolean existsByCode(String code);

    @Query("SELECT s FROM Stablishment s WHERE s.code = :code")
    Optional<Stablishment> findByCode(String code);

    @Query("SELECT s FROM Stablishment s WHERE s.id IN :ids")
    List<Stablishment> findAllByIds(@Param("ids") List<Long> ids);
}
