package com.stablishmentservice.stablishmentservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.stablishmentservice.stablishmentservice.entity.UserStablishment;
import com.stablishmentservice.stablishmentservice.enums.StablishmentError;
import com.stablishmentservice.stablishmentservice.enums.UserStablishmentError;
import com.stablishmentservice.stablishmentservice.exception.StablishmentGeneralException;
import com.stablishmentservice.stablishmentservice.repository.StablishmentRepository;
import com.stablishmentservice.stablishmentservice.repository.UserStablishmentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStablishmentService {
    private final UserStablishmentRepository userStablishmentRepository;
    private final StablishmentRepository stablishmentRepository;

        // Get id by stablishment code
        private Long getStablishmentIdByCode(String stablishmentCode) {
            System.out.println(stablishmentCode);
            Long stablishment = stablishmentRepository.findByCode(stablishmentCode)
                .orElseThrow(() -> new StablishmentGeneralException(StablishmentError.STABLISHMENT_NOT_FOUND))
                .getId();
            System.out.println(stablishment+" service-------------------AHO");
            return stablishment;
        }

    @Transactional
    public void createUserStablishmentRelation(Long userId, String stablishmentCode) {
        System.out.println(stablishmentCode+" pRUEBA-------------------");
        Long stablishmentId = getStablishmentIdByCode(stablishmentCode);
        UserStablishment userStablishment = UserStablishment.builder()
            .userId(userId)
            .stablishmentId(stablishmentId)
            .build();
        userStablishmentRepository.save(userStablishment);
    }
    @Transactional
    public void deleteUserStablishmentRelationsByStablishmentId(String stablishmentCode) {
        Long id = getStablishmentIdByCode(stablishmentCode);
        userStablishmentRepository.deleteAllByStablishmentId(id);
    }
    @Transactional
    public void deleteUserStablishmentRelationsByStablishmentId(Long stablishmentId) {
        userStablishmentRepository.deleteAllByStablishmentId(stablishmentId);
    }

    public List<Long> getAllUsersIdByStablishmentId(Long stablishmentId) {
        return userStablishmentRepository.findAllUsersIdByStablishmentId(stablishmentId);
    }
    @Transactional
    public void deleteUserStablishmentRelations(Long userId) {
        try {
            userStablishmentRepository.deleteByUserId(userId);
        } catch (Exception e) {
            throw new StablishmentGeneralException(UserStablishmentError.USER_STABLISHMENT_RELATION_DELETION_FAILED);
        }
    }
    // create domain service for both serivices
    // Get stablishment code by user id
    
    public UserStablishment getRelationByUserId(Long userId) {
        return userStablishmentRepository.findByUserId(userId)
            .orElseThrow(() -> new StablishmentGeneralException(UserStablishmentError.USER_STABLISHMENT_RELATION_NOT_FOUND));
    }
}