package com.stablishmentservice.stablishmentservice.service;

import org.springframework.boot.security.autoconfigure.SecurityProperties.User;
import org.springframework.stereotype.Service;

import com.stablishmentservice.stablishmentservice.entity.UserStablishment;
import com.stablishmentservice.stablishmentservice.repository.UserStablishmentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStablishmentService {
    private final UserStablishmentRepository userStablishmentRepository;

    @Transactional
    public void createUserStablishmentRelation(Long userId, Long stablishmentId) {
        UserStablishment userStablishment = UserStablishment.builder()
            .userId(userId)
            .stablishmentId(stablishmentId)
            .build();
        userStablishmentRepository.save(userStablishment);
    }
}
