package com.incentiveservice.incentiveservice.v1.service.stablishmentconfiguration;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.incentiveservice.incentiveservice.v1.dto.stablishmentconfiguration.StablishmentConfigurationCreateRequestDto;
import com.incentiveservice.incentiveservice.v1.dto.stablishmentconfiguration.StablishmentConfigurationUpdateRequestDto;
import com.incentiveservice.incentiveservice.v1.entity.StablishmentConfiguration;
import com.incentiveservice.incentiveservice.v1.enums.StablishmentConfigurationError;
import com.incentiveservice.incentiveservice.v1.exception.GeneralException;
import com.incentiveservice.incentiveservice.v1.repository.StablishmentConfigurationRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StablishmentConfigurationCRUDService {
    private final StablishmentConfigurationRepository stablishmentConfigurationRepository;

    public StablishmentConfiguration findByStablishmentCode(
        String stablishmentCode // from user token
    ) {
        return stablishmentConfigurationRepository.findByStablishmentCode(stablishmentCode)
        .orElseThrow(()-> new GeneralException(StablishmentConfigurationError.STABLISHMENT_CONFIGURATION_NOT_FOUND));
    }
    @Transactional
    public StablishmentConfiguration create(
        StablishmentConfigurationCreateRequestDto requestDto
    ) {
        StablishmentConfiguration stablishmentConfiguration = StablishmentConfiguration.builder()
        .stablishmentCode(requestDto.getStablishmentCode())
        .build();
        try {
            return stablishmentConfigurationRepository.save(stablishmentConfiguration);
        } catch (DataIntegrityViolationException e) {
            throw new GeneralException(StablishmentConfigurationError.STABLISHMENT_ALREADY_CONFIGURED);
        } catch (ConstraintViolationException e) {
            throw new GeneralException(StablishmentConfigurationError.INVALID_CONFIGURATION_VALUE);
        } catch (DataAccessException e) {
            throw new GeneralException(StablishmentConfigurationError.INTERNAL_STABLISHMENT_CONFIGURATION_ERROR);
        } 
    }
    @Transactional
    public void update(
        StablishmentConfigurationUpdateRequestDto request,
        String code // from user token
    ) {
        StablishmentConfiguration stablishmentConfiguration = stablishmentConfigurationRepository.findByStablishmentCode(code)
        .orElseThrow(()-> new GeneralException(StablishmentConfigurationError.STABLISHMENT_CONFIGURATION_NOT_FOUND));
        stablishmentConfiguration.setPointsPerEuro(request.getPoint_per_euro());
        stablishmentConfigurationRepository.save(stablishmentConfiguration);
    }
    @Transactional
    public void delete(
        String code
    ) {
        StablishmentConfiguration stablishmentConfiguration = stablishmentConfigurationRepository.findByStablishmentCode(code)
        .orElseThrow(()-> new GeneralException(StablishmentConfigurationError.STABLISHMENT_CONFIGURATION_NOT_FOUND));
        stablishmentConfigurationRepository.delete(stablishmentConfiguration);
    }
}
