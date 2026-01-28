package com.incentiveservice.incentiveservice.v1.service.incentive;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.incentiveservice.incentiveservice.v1.dto.incentive.IncentiveCreateRequestDto;
import com.incentiveservice.incentiveservice.v1.dto.incentive.IncentiveUpdateDto;
import com.incentiveservice.incentiveservice.v1.entity.Incentive;
import com.incentiveservice.incentiveservice.v1.enums.IncentiveError;
import com.incentiveservice.incentiveservice.v1.exception.GeneralException;
import com.incentiveservice.incentiveservice.v1.repository.IncentiveRepository;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncentiveCRUDService {

    private final IncentiveRepository incentiveRepository;

    public List<Incentive> getIncentivesByStablishmentCode(
        String stablishmentCode
    ){
        return incentiveRepository.findAllByStablishmentCode(stablishmentCode);
    }
    public Incentive getIncentivesById(
        Long id
    ){
        return incentiveRepository.findById(id)
        .orElseThrow(()-> new GeneralException(IncentiveError.INCENTIVE_NOT_FOUND));
    }
    public void create(
        IncentiveCreateRequestDto incentiveCreateRequestDto
    ) {
        Incentive incentive = Incentive.builder()
        .productId(incentiveCreateRequestDto.getProductId())
        .stablishmentCode(incentiveCreateRequestDto.getStablishmentCode())
        .pointsRequired(incentiveCreateRequestDto.getPointsRequired())
        .build();
        try {
            incentiveRepository.save(incentive);
        } catch (DataIntegrityViolationException e) {
            throw new GeneralException(IncentiveError.PRODUCT_ALREADY_REGISTERED_AS_INCENTIVE);
        } catch (ConstraintViolationException e) {
            throw new GeneralException(IncentiveError.INVALID_INCENTIVE_DATA);
        } catch (DataAccessException e) {
            throw new GeneralException(IncentiveError.UNEXPECTED_ERROR);
        }
    }
    public void update(
        IncentiveUpdateDto request,
        Long id,
        String codeByToken
    ) {
        Incentive incentive  =incentiveRepository.findById(id).orElseThrow(()-> new GeneralException(IncentiveError.INCENTIVE_NOT_FOUND));
        if (!incentive.getStablishmentCode().equals(codeByToken)) throw new GeneralException(IncentiveError.USER_NOT_AUTHORIZED);
        
        incentive.setPointsRequired(request.getPoinstRequired());
        incentiveRepository.save(incentive);
    }
    public void delete(
        Long id,
        String codeByToken
    ) {
        Incentive incentive = incentiveRepository.findById(id)
        .orElseThrow(()-> new GeneralException(IncentiveError.INCENTIVE_NOT_FOUND));
        if (incentive.getStablishmentCode().equals(codeByToken)) throw new GeneralException(IncentiveError.USER_NOT_AUTHORIZED);
        incentiveRepository.delete(incentive);
    }
    public void deleteAllByStablishmentCode(
        String stablihsmentCode 
    ) {
        incentiveRepository.deleteAllByStablishmentCode(stablihsmentCode);
    }
    public void deleteByProductId(
        Long id
    ) {
        incentiveRepository.deleteByProductId(id);
    }


}
