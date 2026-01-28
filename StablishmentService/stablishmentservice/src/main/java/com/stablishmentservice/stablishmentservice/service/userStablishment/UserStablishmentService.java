package com.stablishmentservice.stablishmentservice.service.userStablishment;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stablishmentservice.stablishmentservice.dto.incentive.points.UserPointsRequest;
import com.stablishmentservice.stablishmentservice.dto.incentive.points.UsersPointsDto;
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
    @Value("${app.servicescredential.incentive-service-user-points-url}")
    private String userPointsUrl;
    private final WebClientService webClientService;

        // Get id by stablishment code
        private Long getStablishmentIdByCode(
            String stablishmentCode
        ) {
            Long stablishment = stablishmentRepository.findByCode(stablishmentCode)
                .orElseThrow(() -> new StablishmentGeneralException(StablishmentError.STABLISHMENT_NOT_FOUND))
                .getId();
            return stablishment;
        }
        private String getStablishmentCodeById(
            Long id
        ) {
            String stablishment = stablishmentRepository.findById(id)
                .orElseThrow(() -> new StablishmentGeneralException(StablishmentError.STABLISHMENT_NOT_FOUND))
                .getCode();
            return stablishment;
        }
    // =========================================================
    // CREATE USER ↔ ESTABLISHMENT RELATION
    // =========================================================
    @Transactional
    public void createUserEmployeeStablishmentRelation(
        Long userId, 
        String stablishmentCode
    ) {
        Long stablishmentId = getStablishmentIdByCode(stablishmentCode);
        userStablishmentCRUDService.create(userId, stablishmentId);
    }
    // overhead to create clients and wallet for points
    @Transactional
    public void createUserCustomerStablishmentRelation(
        String token, 
        String stablishmentCode
    ) {
        //
        token = jwtUtil.cleanJwtToken(token);
        Long userId = jwtUtil.getClaim(token, "id", Long.class);
        Long stablishmentId = getStablishmentIdByCode(stablishmentCode);
        userStablishmentCRUDService.create(userId, stablishmentId);
        try{
            createWalletInIncentiveService(userId, stablishmentCode);
        } catch (StablishmentGeneralException e) {
            userStablishmentCRUDService.deleteByUsertId(userId);
            throw e;
        }
    }
        // =========================================================
        // Incentive service
        private void createWalletInIncentiveService(
            Long userId,
            String stablishmentCode
        ){
            UserPointsRequest userPointsRequest = UserPointsRequest.builder()
            .stablishmentCode(stablishmentCode)
            .userId(userId)
            .build();
            webClientService.securePostMethod(
                userPointsUrl, 
                userPointsRequest, 
                Void.class);
        }
    // ------------------------------------------
    // =========================================================
    // DELETE RELATIONS
    // =========================================================
    @Transactional
    public void deleteUserStablishmentRelationsByStablishmentId(
        String stablishmentCode
    ) {
        Long id = getStablishmentIdByCode(stablishmentCode);
        UsersPointsDto usersPointsDto = deleteAllUserPointsToDeleteStablishment(stablishmentCode);

        try {
            userStablishmentCRUDService.deleteByStablishmentId(id);
        } catch (StablishmentGeneralException e) {
            rollbackUserPointsDeleting(usersPointsDto);
            throw e;
        }
    }
    @Transactional
    public void deleteUserStablishmentRelationsByStablishmentId(
        Long stablishmentId
    ) {
        String code = getStablishmentCodeById(stablishmentId);
        UsersPointsDto usersPointsDto = deleteAllUserPointsToDeleteStablishment(code);
        try {
            userStablishmentCRUDService.deleteByStablishmentId(stablishmentId);
        } catch (StablishmentGeneralException e) {
            rollbackUserPointsDeleting(usersPointsDto);
            throw e;
        }
    }
        private UsersPointsDto deleteAllUserPointsToDeleteStablishment(
            String StablishmentCode
        ) {
            return webClientService.secureDeleteMethod(
                userPointsUrl + "/delete-all-users-wallets-by-stablishment-code/{stablishmentCode}", 
                UsersPointsDto.class, 
                StablishmentCode);
        }
    // =========================================================
    // DELETE RELATION
    // =========================================================
    // delete all relation to delete user
    @Transactional
    public void deleteCustomerStablishmentRelations(
        Long userId
    ) {
        UsersPointsDto usersPointsDto = deleteWalletFromIncentiveServiceByCustomerId(userId);
        
        try{
            userStablishmentCRUDService.deleteByUsertId(userId);
        } catch(StablishmentGeneralException e) {
            rollbackUserPointsDeleting(usersPointsDto);
            throw e;
        }
    }
        // ========================================================= 
        // Incentive service
        // delete all user wallets to delete user
        private UsersPointsDto deleteWalletFromIncentiveServiceByCustomerId(
            Long userId
        ) {
            return webClientService.secureDeleteMethod(
                userPointsUrl + "/delete-all-user-wallets/{userId}", 
                UsersPointsDto.class, 
                userId);
        }
        // Rollback user points deleting
        private void rollbackUserPointsDeleting(
            UsersPointsDto usersPointsDto
        ) {
            webClientService.securePostMethod(
                userPointsUrl + "/rollback-delete", 
                usersPointsDto, 
                Void.class);
        }
        // ========================================================= 
    @Transactional
    public void deleteEmployeeStablishmentRelations(
        Long userId
    ) {
        userStablishmentCRUDService.deleteByUsertId(userId);
    }
    
    public List<Long> getAllUsersIdByStablishmentId(
        Long stablishmentId
    ) {
        return userStablishmentCRUDService.getAllIdsByStablishmentId(stablishmentId);
    }
    public UserStablishment getRelationByUserId(
        Long userId
    ) {
        return userStablishmentCRUDService.getByEmployeeId(userId);    
    }
    public List<UserStablishmentResponseDto> getAllEmployess(
        String token
    ) {
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
    // =========================================================
    // DELETE RELATION CUSTOMER
    // =========================================================
    public void deleteCustomerRelation(
        String token
    ) {
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
        UsersPointsDto usersPointsDto = deleteCustomerWalletFromIncentiveService(userId, stablishmentCode);

        try {
            userStablishmentCRUDService.deleteByUsertIdAndStablishmentId(userId, stablishmentId);
        } catch(StablishmentGeneralException e) {
            rollbackUserPointsDeleting(usersPointsDto);
            throw e;
        }
    }
        // =========================================================
        // Incentive service
        // Delete user wallet by userid and stablishmentCode
        private UsersPointsDto deleteCustomerWalletFromIncentiveService(
            Long userId,
            String stablishmentCode
        ) {
            return webClientService.secureDeleteMethod(
                userPointsUrl + "/{userId}/{stablishmentCode}/delete", 
                UsersPointsDto.class, 
                userId,
                stablishmentCode
            );
        }
        // =========================================================
    public void rollbackDeleteStablishment(
        List<UserStablishment> deletedRelations
    ) {
        userStablishmentCRUDService.saveAll(deletedRelations);
    }
    public List<Long> getStablishmentIdByUserId(
        Long userId
    ) {
        return userStablishmentCRUDService.getByUserId(userId).stream().map(relation -> 
            relation.getStablishmentId()
        )
        .toList();
    }
}