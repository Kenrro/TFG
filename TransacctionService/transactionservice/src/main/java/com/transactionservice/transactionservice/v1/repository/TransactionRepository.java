package com.transactionservice.transactionservice.v1.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.transactionservice.transactionservice.v1.entity.Transaction;
import com.transactionservice.transactionservice.v1.entity.TransactionStatus;
import com.transactionservice.transactionservice.v1.entity.TransactionType;

import jakarta.transaction.Transactional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>{

    List<Transaction> findByCustomerId(Long customerId);

    List<Transaction> findByStatus(TransactionStatus status);

    List<Transaction> findByStablishmentCode(String stablishmentCode);

    List<Transaction> findByStablishmentCodeAndType(String stablishmentCode, TransactionType type);

    @Modifying
    @Transactional
    @Query("DELETE FROM Transaction t WHERE t.stablishmentCode = :stablishmentCode")
    void deleteAllByStablishmentCode(@Param("stablishmentCode") String stablishmentCode);
}
