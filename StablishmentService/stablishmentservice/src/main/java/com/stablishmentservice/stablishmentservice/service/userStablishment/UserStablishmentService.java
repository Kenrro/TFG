package com.stablishmentservice.stablishmentservice.service.userStablishment;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stablishmentservice.stablishmentservice.dto.stablishment.UserStablishmentCreateCustomerRelationRequestDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.UserStablishmentResponseDto;
import com.stablishmentservice.stablishmentservice.entity.UserStablishment;
import com.stablishmentservice.stablishmentservice.enums.StablishmentError;
import com.stablishmentservice.stablishmentservice.exception.StablishmentGeneralException;
import com.stablishmentservice.stablishmentservice.jwt.JwtUtil;
import com.stablishmentservice.stablishmentservice.repository.StablishmentRepository;
import com.stablishmentservice.stablishmentservice.service.WebClientService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStablishmentService {
    private final StablishmentRepository stablishmentRepository;
    private final UserStablishmentCRUDService userStablishmentCRUDService;
    private final JwtUtil jwtUtil;
    @Value("${app.servicescredential.authServiceUrl}")
    private String authServiceUrl;
    private final WebClientService webClientService;

        // Get id by stablishment code
        private Long getStablishmentIdByCode(String stablishmentCode) {
            Long stablishment = stablishmentRepository.findByCode(stablishmentCode)
                .orElseThrow(() -> new StablishmentGeneralException(StablishmentError.STABLISHMENT_NOT_FOUND))
                .getId();
            return stablishment;
        }

    @Transactional
    public void createUserStablishmentRelation(Long userId, String stablishmentCode) {
        Long stablishmentId = getStablishmentIdByCode(stablishmentCode);
        userStablishmentCRUDService.create(userId, stablishmentId);
        
    }
    @Transactional
    public void deleteUserStablishmentRelationsByStablishmentId(String stablishmentCode) {
        Long id = getStablishmentIdByCode(stablishmentCode);
        userStablishmentCRUDService.deleteByStablishmentId(id);
    }
    @Transactional
    public void deleteUserStablishmentRelationsByStablishmentId(Long stablishmentId) {
        userStablishmentCRUDService.deleteByStablishmentId(stablishmentId);
    }
    
    public List<Long> getAllUsersIdByStablishmentId(Long stablishmentId) {
        return userStablishmentCRUDService.getAllIdsByStablishmentId(stablishmentId);
    }
    @Transactional
    public void deleteUserStablishmentRelations(Long userId) {
        userStablishmentCRUDService.deleteByUsertId(userId);
        
    }
    public UserStablishment getRelationByUserId(Long userId) {
        return userStablishmentCRUDService.getByEmployeeId(userId);    
    }
    public List<UserStablishmentResponseDto> getAllEmployess(String token) {
        token = jwtUtil.cleanJwtToken(token);
        String stablishmentCode = jwtUtil.getClaim(token, "establishmentCode", String.class);
        Long stablishmentId = getStablishmentIdByCode(stablishmentCode);
        return userStablishmentCRUDService.getByStablishmentId(stablishmentId)
            .stream()
            .map(relation -> UserStablishmentResponseDto.builder()
                                .userId(relation.getUserId())
                                .stablishmentId(relation.getStablishmentId())
                                .build())
            .toList();
    }
    public void createCustomerRelation(UserStablishmentCreateCustomerRelationRequestDto request,
                                        String token) {
        token = jwtUtil.cleanJwtToken(token);
        String username = jwtUtil.getClaim(token, "sub", String.class);
        Long userId = webClientService.secureGetMethod(authServiceUrl + "/get-user-id-by-username/{username}",
                                                             Long.class,
                                                              username);
        Long stablishmentId = getStablishmentIdByCode(request.getStablishmentCode());
        userStablishmentCRUDService.create(userId, stablishmentId);
    }
    public void deleteCustomerRelation(String token) {
        token = jwtUtil.cleanJwtToken(token);
        Long userId = jwtUtil.getClaim(token, "id", Long.class);
        userStablishmentCRUDService.deleteByUsertId(userId);
    }
    public void deleteCustomerRelationByCodeAndUserId(
        String token,
        String stablishmentCode) {

        token = jwtUtil.cleanJwtToken(token);
        Long stablishmentId = getStablishmentIdByCode(stablishmentCode);
        Long userId = jwtUtil.getClaim(token, "id", Long.class);
        userStablishmentCRUDService.deleteByUsertIdAndStablishmentId(userId, stablishmentId);
    }
    public void rollbackDeleteStablishment(List<UserStablishment> deletedRelations) {
        userStablishmentCRUDService.saveAll(deletedRelations);
    }
    public List<Long> getStablishmentIdByUserId(Long userId) {
        return userStablishmentCRUDService.getByUserId(userId).stream().map(relation -> 
            relation.getStablishmentId()
        )
        .toList();
    }
}