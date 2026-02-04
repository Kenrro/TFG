package com.incentiveservice.incentiveservice.v1.service.transaction;


import org.springframework.stereotype.Service;

import com.incentiveservice.incentiveservice.v1.dto.transaction.TransactionPointsDto;
import com.incentiveservice.incentiveservice.v1.dto.transaction.TransactionPointsResponseDto;
import com.incentiveservice.incentiveservice.v1.dto.transaction.TransactionRedeemDto;
import com.incentiveservice.incentiveservice.v1.entity.Incentive;
import com.incentiveservice.incentiveservice.v1.enums.UserPointsError;
import com.incentiveservice.incentiveservice.v1.exception.GeneralException;
import com.incentiveservice.incentiveservice.v1.service.incentive.IncentiveCRUDService;
import com.incentiveservice.incentiveservice.v1.service.stablishmentconfiguration.StablishmentConfigurationCRUDService;

import com.incentiveservice.incentiveservice.v1.service.userpoints.UserPointsService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionProcessService {

    private final IncentiveCRUDService incentiveService;
    private final UserPointsService userPointsService;
    private final StablishmentConfigurationCRUDService stablishmentConfigurationService;
    // =========================================================
    // PROCEES POINTS
    // =========================================================
    @Transactional
    public TransactionPointsResponseDto processPoints(
        TransactionPointsDto request
    ) {
        if (request.getAmountSpent().intValue() < 1 ) throw new GeneralException(UserPointsError.INSUFFICIENT_USER_POINTS);
        // 1. obtain the establishment's configuration and calculate the points to be awarded
        int pointsPerEuro = stablishmentConfigurationService        
            .findByStablishmentCode(request.getStablishmentCode()).getPointsPerEuro();
        int poinst = Math.round( //points to be awarded
            pointsPerEuro * request.getAmountSpent().intValue()
        );
        // 2. add points to the user
        userPointsService.addPoints(
            request.getCustomerId(), 
            request.getStablishmentCode(), 
            poinst);

        return TransactionPointsResponseDto.builder()
        .pointsInvolved(poinst)
        .build();
    }
    // =========================================================
    // PROCESS REDEEM
    // =========================================================
    @Transactional
    public TransactionPointsResponseDto processRedeem(
        TransactionRedeemDto request
    ) {
        // 1. obtain incentive
        Incentive incentive =
            incentiveService.getIncentivesById(request.getIncentiveId());
        // 2. try to remove points
        userPointsService.subtractPoints(
            request.getCustomerId(), 
            request.getStablishmentCode(), 
            incentive.getPointsRequired());

        return TransactionPointsResponseDto.builder()
        .pointsInvolved(incentive.getPointsRequired())
        .build(); 
    }
}
