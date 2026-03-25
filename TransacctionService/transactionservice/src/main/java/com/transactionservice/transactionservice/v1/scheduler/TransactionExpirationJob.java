package com.transactionservice.transactionservice.v1.scheduler;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.transactionservice.transactionservice.v1.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TransactionExpirationJob {
    private final TransactionRepository transactionRepository;

    @Scheduled(fixedRate = 7200000)
    public void expireTransactions() {
        int updated = transactionRepository
            .expireOldTransactions(Instant.now());
    }
}
