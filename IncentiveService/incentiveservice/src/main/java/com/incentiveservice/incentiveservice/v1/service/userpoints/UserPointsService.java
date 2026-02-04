package com.incentiveservice.incentiveservice.v1.service.userpoints;

import java.util.List;

import org.springframework.stereotype.Service;

import com.incentiveservice.incentiveservice.v1.dto.userpoints.UserPointsDto;
import com.incentiveservice.incentiveservice.v1.dto.userpoints.UsersPointsDto;
import com.incentiveservice.incentiveservice.v1.entity.UserPoints;
import com.incentiveservice.incentiveservice.v1.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPointsService {
    private final UserPointsCRUDService userPointsCRUDService;
    private final JwtUtil jwtUtil;
    public void create(
        Long id,
        String stablishmentCode
    ) {
        userPointsCRUDService.create(id, stablishmentCode);
    }
    public void createAll(
        UsersPointsDto request
    ) {
        userPointsCRUDService.saveAll(request);
    }

    public UserPointsDto getUserPointsByUserIdAndStablishmentCode(
        String token,
        String stablishmentCode
    ) {
        token = jwtUtil.cleanJwtToken(token);
        Long id = jwtUtil.getClaim(token, "id", Long.class);
        UserPoints userPoints = userPointsCRUDService.getByIdAndStablishmentCode(id, stablishmentCode);
        return UserPointsDto.builder()
            .balance(userPoints.getBalance())
            .userId(userPoints.getUserId())
            .stablishmentCode(userPoints.getStablishmentCode())
            .build();
    }
    public void addPoints(
        Long userId,
        String stablishmentCode,
        int amount
    ) {
        userPointsCRUDService.addPoints(userId, stablishmentCode, amount);
    }
    public void subtractPoints(
        Long userId,
        String stablishmentCode,
        int amount
    ) {
        userPointsCRUDService.removePoints(userId, stablishmentCode, amount);
    }

    public UsersPointsDto deleteUserPoints(
        Long userId,
        String stablishmentCode
    ) {
        UserPoints userPoints = userPointsCRUDService.deleteUserPoints(userId, stablishmentCode);
        UserPointsDto userPointsDto = UserPointsDto.builder()
        .userId(userPoints.getUserId())
        .balance(userPoints.getBalance())
        .stablishmentCode(userPoints.getStablishmentCode())
        .build();
        return UsersPointsDto.builder()
        .userPoints(List.of(userPointsDto))
        .build();
    }

    public UsersPointsDto deleteAllUserPointsByUserId(Long userId) {
        return UsersPointsDto.builder()
        .userPoints(
            
            userPointsCRUDService.deleteByUserId(userId)
            .stream()
            .map(userPoints -> 
                UserPointsDto.builder()
                .balance(userPoints.getBalance())
                .userId(userPoints.getUserId())
                .stablishmentCode(userPoints.getStablishmentCode())
                .build()
            )
            .toList()
        ).build();
        
    }
    public UsersPointsDto deleteAllUserPointsByStablishmentCode(String code) {
        return UsersPointsDto.builder()
        .userPoints(
            userPointsCRUDService.deleteByStablishmentCode(code)
            .stream()
            .map(userPoints -> 
                UserPointsDto.builder()
                .balance(userPoints.getBalance())
                .userId(userPoints.getUserId())
                .stablishmentCode(userPoints.getStablishmentCode())
                .build()
            )
            .toList()
        ).build();
    }
}

