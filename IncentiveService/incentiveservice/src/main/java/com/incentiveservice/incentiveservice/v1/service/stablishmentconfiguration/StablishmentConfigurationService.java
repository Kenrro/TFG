package com.incentiveservice.incentiveservice.v1.service.stablishmentconfiguration;


import org.springframework.stereotype.Service;

import com.incentiveservice.incentiveservice.v1.dto.stablishmentconfiguration.StablishmentConfigurationCreateRequestDto;
import com.incentiveservice.incentiveservice.v1.dto.stablishmentconfiguration.StablishmentConfigurationResponseDto;
import com.incentiveservice.incentiveservice.v1.dto.stablishmentconfiguration.StablishmentConfigurationUpdateRequestDto;
import com.incentiveservice.incentiveservice.v1.entity.StablishmentConfiguration;
import com.incentiveservice.incentiveservice.v1.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StablishmentConfigurationService {
    private final StablishmentConfigurationCRUDService stablishmentConfigurationCRUDService;
    private final JwtUtil jwtUtil;
    public StablishmentConfigurationResponseDto getByStablishmentCode(
        String token
    ) {
        token = jwtUtil.cleanJwtToken(token);
        String code = jwtUtil.getClaim(token, "establishmentCode", String.class);
        StablishmentConfiguration stablishmentConfiguration = stablishmentConfigurationCRUDService.findByStablishmentCode(code);
        return StablishmentConfigurationResponseDto.builder()
        .stablishmentCode(stablishmentConfiguration.getStablishmentCode())
        .points_per_euro(stablishmentConfiguration.getPointsPerEuro())
        .build();
    }
    public StablishmentConfigurationResponseDto createStablishmentConfiguration(
        StablishmentConfigurationCreateRequestDto request
    ) {
        StablishmentConfiguration configuration = stablishmentConfigurationCRUDService.create(request);
        return StablishmentConfigurationResponseDto.builder()
        .stablishmentCode(configuration.getStablishmentCode())
        .points_per_euro(configuration.getPointsPerEuro())
        .build();

    }
    public void updateStablishmentConfiguration(
        StablishmentConfigurationUpdateRequestDto request,
        String token
    ) {
        token = jwtUtil.cleanJwtToken(token);
        String code = jwtUtil.getClaim(token, "establishmentCode", String.class);
        stablishmentConfigurationCRUDService.update(request, code);
    }
    public void deleteStablishmentConfiguration(
        String stablishmentCode
    ) {
        stablishmentConfigurationCRUDService.delete(stablishmentCode);
    }
}
