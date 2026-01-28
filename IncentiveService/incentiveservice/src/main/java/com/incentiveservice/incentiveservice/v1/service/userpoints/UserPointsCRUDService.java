package com.incentiveservice.incentiveservice.v1.service.userpoints;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.incentiveservice.incentiveservice.v1.dto.userpoints.UsersPointsDto;
import com.incentiveservice.incentiveservice.v1.entity.UserPoints;
import com.incentiveservice.incentiveservice.v1.enums.UserPointsError;
import com.incentiveservice.incentiveservice.v1.exception.GeneralException;
import com.incentiveservice.incentiveservice.v1.repository.UserPointsRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPointsCRUDService {

    private final UserPointsRepository userPointsRepository;

    public UserPoints getByIdAndStablishmentCode(
        Long userId,
        String stablihsmentCode
    ) {
        return userPointsRepository.findByUserIdAndCode(userId, stablihsmentCode).orElseThrow(()->
        new GeneralException(UserPointsError.USER_POINTS_NOT_FOUND));
    }
    public void create(
        Long userId,
        String stablishmentCode
    ) 
    {
        try {
            UserPoints userPoints = UserPoints.builder()
                .userId(userId)
                .stablishmentCode(stablishmentCode)
                .balance(0)
                .build();

            userPointsRepository.save(userPoints);

        } catch (DataIntegrityViolationException e) {
            throw new GeneralException(
                UserPointsError.USER_POINTS_CREATION_FAILED
            );

        } catch (ConstraintViolationException e) {
            throw new GeneralException(
                UserPointsError.INVALID_USER_ID
            );
        }
    }
    @Transactional
    public void addPoints(
        Long userId,
        String stablishmentCode, 
        int amount) {
            UserPoints userPoints = userPointsRepository.findByUserIdAndCode(userId, stablishmentCode).orElseThrow(()->
        new GeneralException(UserPointsError.USER_POINTS_NOT_FOUND));
        try {
            userPoints.addPoints(amount); 
            userPointsRepository.save(userPoints); 
        } catch (Exception e) {
            throw new GeneralException(UserPointsError.USER_POINTS_UPDATE_FAILED);
        }

    }
    @Transactional
    public void removePoints(
        Long userId,
        String stablishmentCode,
        int amount) {
            UserPoints userPoints = userPointsRepository.findByUserIdAndCode(userId, stablishmentCode).orElseThrow(()->
        new GeneralException(UserPointsError.USER_POINTS_NOT_FOUND));
        try {
            userPoints.subtractPoints(amount); 
            userPointsRepository.save(userPoints); 
        } catch (Exception e) {
            throw new GeneralException(UserPointsError.USER_POINTS_UPDATE_FAILED);
        }
    }
    @Transactional
    public UserPoints deleteUserPoints(
        Long userId,
        String stablishmentCode) {
            System.out.println(userId + stablishmentCode);
            UserPoints userPoints = userPointsRepository.findByUserIdAndCode(userId, stablishmentCode).orElseThrow(()->
        new GeneralException(UserPointsError.USER_POINTS_NOT_FOUND));

        userPointsRepository.delete(userPoints);
        return userPoints;
    }
    @Transactional
    public List<UserPoints> deleteByUserId(
        Long userId
    ) {
        List<UserPoints> userPoints = userPointsRepository.findAllByUserId(userId);
        userPointsRepository.deleteAll(userPoints);
        return userPoints;
    }
    @Transactional
    public List<UserPoints> deleteByStablishmentCode(
        String code
    ) {
        List<UserPoints> userPoints = userPointsRepository.findAllByStablishmentCode(code);
        userPointsRepository.deleteAll(userPoints);
        return userPoints;
    }
    public void saveAll(UsersPointsDto request) {
        try {
            List<UserPoints> usersPoints = request.getUserPoints()
            .stream()
            .map(userPoints ->
                UserPoints.builder()
                    .id(userPoints.getUserId())
                    .userId(userPoints.getUserId())
                    .stablishmentCode(userPoints.getStablishmentCode())
                    .balance(0)
                    .build()
            ).toList();
            userPointsRepository.saveAll(usersPoints);
        } catch (DataIntegrityViolationException e) {
            throw new GeneralException(
                UserPointsError.USER_POINTS_CREATION_FAILED
            );

        } catch (ConstraintViolationException e) {
            throw new GeneralException(
                UserPointsError.INVALID_USER_ID
            );
        }
    }

}
