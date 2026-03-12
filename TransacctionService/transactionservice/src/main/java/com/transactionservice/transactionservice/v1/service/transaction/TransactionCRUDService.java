package com.transactionservice.transactionservice.v1.service.transaction;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.transactionservice.transactionservice.v1.dto.transaction.CreateGivePointsTransactionRequestDto;
import com.transactionservice.transactionservice.v1.dto.transaction.CreateRedeemTransactionRequestDto;
import com.transactionservice.transactionservice.v1.entity.GivePointsTransaction;
import com.transactionservice.transactionservice.v1.entity.RedeemProductTransaction;
import com.transactionservice.transactionservice.v1.entity.Transaction;
import com.transactionservice.transactionservice.v1.entity.TransactionType;
import com.transactionservice.transactionservice.v1.enums.TransactionError;
import com.transactionservice.transactionservice.v1.exception.GeneralException;
import com.transactionservice.transactionservice.v1.entity.TransactionStatus;
import com.transactionservice.transactionservice.v1.repository.TransactionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionCRUDService {

    private final TransactionRepository repository;

    public Transaction findExistingTransaction(Long userId, TransactionType type, String stablishmentCode) {
        Transaction transaction = repository.findExistingTransaction(userId, userId, stablishmentCode, type).stream().findFirst().orElse(null);
        return transaction != null ? transaction : null;
    }

    @Transactional
    public Transaction createRedeemTransaction(
            CreateRedeemTransactionRequestDto request,
            Long id
    ) {
        Instant now = Instant.now();
        RedeemProductTransaction transaction =
            RedeemProductTransaction.builder()
                .type(TransactionType.REDEEM_PRODUCT)
                .status(TransactionStatus.PENDING)
                .stablishmentCode(request.getStablishmentCode())
                .employeeId(null)
                .customerId(id)
                .productId(request.getProductId())
                .incentiveId(request.getIncentiveId())
                .pointsRequired(0)
                .createdAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .build();

        try {
            return repository.save(transaction);

        } catch (DataIntegrityViolationException e) {
            throw new GeneralException(
                TransactionError.INVALID_TRANSACTION_DATA
            );

        } catch (DataAccessException e) {
            throw new GeneralException(
                TransactionError.UNEXPECTED_ERROR
            );
        }
    }
    @Transactional
    public Transaction createGivePointsTransaction(
        CreateGivePointsTransactionRequestDto request,
        String code,
        Long employeeId
    ) {
        GivePointsTransaction transaction =
        GivePointsTransaction.builder()
        .status(TransactionStatus.PENDING)
        .type(TransactionType.EARN_POINTS)
        .stablishmentCode(code)
        .employeeId(employeeId)
        .customerId(null)
        .pointsGiven(0) // pendiente hasta la confirmación
        .amountSpent(request.getAmountSpent())
        .createdAt(Instant.now())
        .expiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
        .build();

        try {
            return repository.save(transaction);

        } catch (DataIntegrityViolationException e) {
            throw new GeneralException(
                TransactionError.INVALID_TRANSACTION_DATA
            );

        } catch (DataAccessException e) {
            throw new GeneralException(
                TransactionError.UNEXPECTED_ERROR
            );
        }
    }
    public Transaction findById(
        UUID id
    ) {
        return repository.findById(id).orElseThrow(() -> 
            new GeneralException(TransactionError.TRANSACTION_NOT_FOUND)
        );
    }
    public List<Transaction> findByStablishmentAndType(
        String stablishmentCode,
        TransactionType type
    ) {
        return repository.findByStablishmentCodeAndType(stablishmentCode, type);
    }
    @Transactional
    public Transaction saveTransaction(
        Transaction transaction
    ) {
        try {
            return repository.save(transaction);
        } catch (DataIntegrityViolationException e) {
            throw new GeneralException(TransactionError.INVALID_TRANSACTION_DATA);
        } catch (DataAccessException e) {
            throw new GeneralException(TransactionError.UNEXPECTED_ERROR);
        }
    }

    @Transactional
    public void deleteAllByStablishmentCode(
        String code
    ) {
        repository.deleteAllByStablishmentCode(code);
    }
}
