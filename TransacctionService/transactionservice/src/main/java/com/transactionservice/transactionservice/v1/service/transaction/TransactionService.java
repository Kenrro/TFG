package com.transactionservice.transactionservice.v1.service.transaction;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.transactionservice.transactionservice.v1.dto.transaction.CreateGivePointsTransactionRequestDto;
import com.transactionservice.transactionservice.v1.dto.transaction.CreateRedeemTransactionRequestDto;
import com.transactionservice.transactionservice.v1.dto.transaction.GivePointsDto;
import com.transactionservice.transactionservice.v1.dto.transaction.GivePointsTransactionsResponseDto;
import com.transactionservice.transactionservice.v1.dto.transaction.IncentiveQuantityResponseDto;
import com.transactionservice.transactionservice.v1.dto.transaction.RedeemProductDto;
import com.transactionservice.transactionservice.v1.dto.transaction.RedeemProductTransactionsResponseDto;
import com.transactionservice.transactionservice.v1.dto.transaction.TransactionInformationDto;
import com.transactionservice.transactionservice.v1.dto.transaction.TransactionUUIDDto;
import com.transactionservice.transactionservice.v1.dto.transactionprocess.TransactionPointsDto;
import com.transactionservice.transactionservice.v1.dto.transactionprocess.TransactionPointsResponseDto;
import com.transactionservice.transactionservice.v1.dto.transactionprocess.TransactionRedeemDto;
import com.transactionservice.transactionservice.v1.entity.GivePointsTransaction;
import com.transactionservice.transactionservice.v1.entity.RedeemProductTransaction;
import com.transactionservice.transactionservice.v1.entity.Transaction;
import com.transactionservice.transactionservice.v1.entity.TransactionStatus;
import com.transactionservice.transactionservice.v1.entity.TransactionType;
import com.transactionservice.transactionservice.v1.enums.TransactionError;
import com.transactionservice.transactionservice.v1.exception.GeneralException;
import com.transactionservice.transactionservice.v1.jwt.JwtUtil;
import com.transactionservice.transactionservice.v1.service.WebClientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
        
    @Value("${app.servicescredential.transaction-process-url}") String transactionProcessUrl;
    private final TransactionCRUDService crudService;
    private final WebClientService webClientService;
    private final JwtUtil jwtUtil;
    // =========================================================
    // CREATE GIVE POINTS TRANSACTIONAL
    // =========================================================
    // Create give points
    public TransactionUUIDDto createGivePointsTransaction(
        String token,
        CreateGivePointsTransactionRequestDto request
    ) {
        token = jwtUtil.cleanJwtToken(token);
        String code = jwtUtil.getClaim(token, "establishmentCode", String.class);
        Long id = jwtUtil.getClaim(token, "id", Long.class);
        Transaction transaction = (GivePointsTransaction) crudService.findExistingTransaction(id, TransactionType.EARN_POINTS, code);
        if (transaction != null) {
            if (transaction instanceof GivePointsTransaction givePointsTransaction) {
                givePointsTransaction.setAmountSpent(request.getAmountSpent());
                givePointsTransaction.setCreatedAt(Instant.now());
                givePointsTransaction.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));
            } else {
                throw new GeneralException(TransactionError.INVALID_TRANSACTION_TYPE);
            }
        } else {
            transaction = crudService.createGivePointsTransaction(request, code, id);
        }
        return TransactionUUIDDto.builder()
        .id(transaction.getId())
        .build();
    }
    // Procces Give Points
    // Saga
    public void processGivePoints(
        String token,
        TransactionUUIDDto request
    ) {
        // get id
        token = jwtUtil.cleanJwtToken(token);
        Long customerId = jwtUtil.getClaim(token, "id", Long.class);
        Transaction tx = crudService.findById(request.getId());
        // validate class
        if (!(tx instanceof GivePointsTransaction transaction)) {
            throw new GeneralException(TransactionError.INVALID_TRANSACTION_TYPE);
        }
        if (!transaction.getStatus().equals(TransactionStatus.PENDING)) throw new GeneralException(TransactionError.TRANSACTION_ALREADY_PROCESSED);
        // validate exp
        if (transaction.getExpiresAt().isBefore(Instant.now())) {
            transaction.setStatus(TransactionStatus.EXPIRED);
            crudService.saveTransaction(transaction);
            throw new GeneralException(TransactionError.TRANSACTION_EXPIRED);
        }
        transaction.setStatus(TransactionStatus.PROCESSING);

        //
        transaction.setCustomerId(customerId);
        // Build request
        TransactionPointsDto pointsRequest = TransactionPointsDto.builder()
        .amountSpent(transaction.getAmountSpent())
        .customerId(transaction.getCustomerId())
        .stablishmentCode(transaction.getStablishmentCode())
        .build();
        System.out.println("amount: ---------------------"+ pointsRequest.getAmountSpent());
        // Process transaction
        try {
            TransactionPointsResponseDto response =
            webClientService.securePostMethod(
                transactionProcessUrl + "/points", 
                pointsRequest, 
                TransactionPointsResponseDto.class);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setPointsGiven(response.getPointsInvolved());
        } catch(GeneralException e) {
            transaction.setStatus(TransactionStatus.FAILED);
            throw e;
        } finally {
            crudService.saveTransaction(transaction);
        }

        
    }
    // =========================================================
    // CREATE REDEEM TRANSACTION
    // =========================================================
    // CREATE REDDEM TRANSACTION
    public TransactionUUIDDto createRedeemTransaction(
        CreateRedeemTransactionRequestDto request,
        String token
    ) {
        token = jwtUtil.cleanJwtToken(token);
        Long id = jwtUtil.getClaim(token, "id", Long.class);
        Transaction transaction = crudService.findExistingTransaction(id, TransactionType.REDEEM_PRODUCT, request.getStablishmentCode());
        if (transaction != null) {
            if (transaction instanceof RedeemProductTransaction redeemTransaction) {
                redeemTransaction.setIncentiveId(request.getIncentiveId());
                redeemTransaction.setProductId(request.getProductId());
                redeemTransaction.setCreatedAt(Instant.now());
                redeemTransaction.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));
            } else {
                throw new GeneralException(TransactionError.INVALID_TRANSACTION_TYPE);
            }
        } else {
             transaction = crudService.createRedeemTransaction(request, id);
            
        }
        return TransactionUUIDDto.builder()
        .id(transaction.getId())
        .build();
    }
    // Procces redeem incentive
    // Saga
    public void processRedeemTransaction(
        String token,
        TransactionUUIDDto request
    ) {
        // get id
        token = jwtUtil.cleanJwtToken(token);
        Long employeeId = jwtUtil.getClaim(token, "id", Long.class);
        Transaction tx = crudService.findById(request.getId());
        // validate class
        if (!(tx instanceof RedeemProductTransaction transaction)) {
            throw new GeneralException(TransactionError.INVALID_TRANSACTION_TYPE);
        }
        if (!transaction.getStatus().equals(TransactionStatus.PENDING)) throw new GeneralException(TransactionError.TRANSACTION_ALREADY_PROCESSED);
        // validate exp
        if (transaction.getExpiresAt().isBefore(Instant.now())) {
            transaction.setStatus(TransactionStatus.EXPIRED);
            crudService.saveTransaction(transaction);
            throw new GeneralException(TransactionError.TRANSACTION_EXPIRED);
        }
        transaction.setEmployeeId(employeeId);
        //
        // Build request
        TransactionRedeemDto redeemRequest = TransactionRedeemDto.builder()
        .customerId(transaction.getCustomerId())
        .incentiveId(transaction.getIncentiveId())
        .stablishmentCode(transaction.getStablishmentCode())
        .build();
        // Process redeem transaction
        try {
            TransactionPointsResponseDto response =
            webClientService.securePostMethod(
                transactionProcessUrl + "/redeem", 
                redeemRequest, 
                TransactionPointsResponseDto.class);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setPointsRequired(response.getPointsInvolved());
        } catch(GeneralException e) {
            transaction.setStatus(TransactionStatus.FAILED);
            throw e;
        } finally {
            crudService.saveTransaction(transaction);
        }
    }
    // =========================================================
    // GIVE POINTS TRANSACTIONS
    // =========================================================
    public GivePointsTransactionsResponseDto getGivePointsTransactions(
        String token
    ) {
        token = jwtUtil.cleanJwtToken(token);
        String stablishmentCode = jwtUtil.getClaim(
            token, 
            "establishmentCode", 
            String.class);
        List<Transaction> transactions = crudService.findByStablishmentAndType(
            stablishmentCode, TransactionType.EARN_POINTS);
        List<GivePointsTransaction> givePointsTransactions =
        transactions.stream()
            .filter(tx -> tx instanceof GivePointsTransaction)
            .map(tx -> (GivePointsTransaction) tx)
            .toList();
        return GivePointsTransactionsResponseDto.builder()
        .transactions(
            givePointsTransactions.stream().map(transaction -> 
                GivePointsDto.builder()
                .amountSpent(transaction.getAmountSpent())
                .createdAt(transaction.getCreatedAt())
                .customerId(transaction.getCustomerId())
                .employeeId(transaction.getEmployeeId())
                .expiresAt(transaction.getExpiresAt())
                .pointsGiven(transaction.getPointsGiven())
                .type(transaction.getType())
                .stablishmentCode(transaction.getStablishmentCode())
                .status(transaction.getStatus())
                .id(transaction.getId())
                .build()
            ).toList()
        ).build();
        
    }
    // =========================================================
    // GET REDEEM PRODUCT TRANSACTION
    // =========================================================
    public RedeemProductTransactionsResponseDto getRedeemProductTransaction(
        String token
    ) {
        token = jwtUtil.cleanJwtToken(token);
        String stablishmentCode = jwtUtil.getClaim(
            token, 
            "establishmentCode", 
            String.class);
        List<Transaction> transactions = crudService.findByStablishmentAndType(
            stablishmentCode, TransactionType.REDEEM_PRODUCT);
        List<RedeemProductTransaction> redeemProductTransactions =
        transactions.stream()
            .filter(tx -> tx instanceof RedeemProductTransaction)
            .map(tx -> (RedeemProductTransaction) tx)
            .toList();
        return RedeemProductTransactionsResponseDto.builder()
        .transactions(
            redeemProductTransactions.stream().map(transaction -> 
                RedeemProductDto.builder()
                .createdAt(transaction.getCreatedAt())
                .expiresAt(transaction.getExpiresAt())
                .employeeId(transaction.getEmployeeId())
                .customerId(transaction.getCustomerId())
                .id(transaction.getId())
                .status(transaction.getStatus())
                .type(transaction.getType())
                .incentiveId(transaction.getCustomerId())
                .pointsRequired(transaction.getPointsRequired())
                .productId(transaction.getProductId())
                .stablishmentCode(transaction.getStablishmentCode())
                .build()
            ).toList()
        ).build();
        
    }
    // =========================================================
    // GET TRANSACTIONS INFORMATION
    // =========================================================

    public TransactionInformationDto getTransactionsInformation(
        String stablishmentCode
    ) {
        List<Transaction> transactions = crudService.findByStablishment(stablishmentCode);
        List<RedeemProductTransaction> redeemProductTransactions =
        transactions.stream()
            .filter(tx -> tx instanceof RedeemProductTransaction)
            .map(tx -> (RedeemProductTransaction) tx)
            .toList();
        int redeemedProducts = redeemProductTransactions.size();
        int pointsAwarded = transactions.stream()
            .filter(tx -> tx instanceof GivePointsTransaction)
            .map(tx -> (GivePointsTransaction) tx)
            .mapToInt(GivePointsTransaction::getPointsGiven)
            .sum();
        return TransactionInformationDto.builder()
        .pointsAwarded(pointsAwarded)
        .redeemedProducts(redeemedProducts)
        .build();
    }
    // =========================================================
    // GET INCENTIVE QUANTIY
    // =========================================================
    public List<IncentiveQuantityResponseDto> getIncentiveQuantity(String token) {

        token = jwtUtil.cleanJwtToken(token);
        String stablishmentCode = jwtUtil.getClaim(
            token,
            "establishmentCode",
            String.class
        );

        List<Transaction> transactions = crudService.findByStablishment(stablishmentCode);

        Map<Long, Integer> counter = new HashMap<>();

        for (Transaction tx : transactions) {
            if (tx instanceof RedeemProductTransaction redeem) {

                Long incentiveId = redeem.getIncentiveId();

                counter.put(
                    incentiveId,
                    counter.getOrDefault(incentiveId, 0) + 1
                );
            }
        }

        return counter.entrySet().stream()
            .map(entry -> IncentiveQuantityResponseDto.builder()
                .incentiveId(entry.getKey())
                .quantity(entry.getValue())
                .build()
            )
            .toList();
    }
}
