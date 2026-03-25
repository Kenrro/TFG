package com.stablishmentservice.stablishmentservice.service.userStablishment;

import java.util.List;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stablishmentservice.stablishmentservice.dto.authorization.UsersDto;
import com.stablishmentservice.stablishmentservice.dto.authorization.UsersIdsRequestDto;
import com.stablishmentservice.stablishmentservice.dto.incentive.points.UserPointsRequest;
import com.stablishmentservice.stablishmentservice.dto.incentive.points.UsersPointsDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.RelationUserStablishmentDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.UsersRelationsResponseDto;
import com.stablishmentservice.stablishmentservice.entity.UserStablishment;
import com.stablishmentservice.stablishmentservice.enums.StablishmentError;
import com.stablishmentservice.stablishmentservice.exception.GeneralException;
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
                .orElseThrow(() -> new GeneralException(StablishmentError.STABLISHMENT_NOT_FOUND))
                .getId();
            return stablishment;
        }
        private String getStablishmentCodeById(
            Long id
        ) {
            String stablishment = stablishmentRepository.findById(id)
                .orElseThrow(() -> new GeneralException(StablishmentError.STABLISHMENT_NOT_FOUND))
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
    // =========================================================
    // CREATE USER ↔ ESTABLISHMENT RELATION
    // =========================================================
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
        } catch (GeneralException e) {
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
    // =========================================================
    // DELETE ALL STABLISHMENT RELATIONS
    // =========================================================
    @Transactional
    public void deleteUserStablishmentRelationsByStablishmentId(
        String stablishmentCode
    ) {
        Long id = getStablishmentIdByCode(stablishmentCode);
        UsersPointsDto usersPointsDto = deleteAllUserPointsToDeleteStablishment(stablishmentCode);

        try {
            userStablishmentCRUDService.deleteByStablishmentId(id);
        } catch (GeneralException e) {
            rollbackUserPointsDeleting(usersPointsDto);
            throw e;
        }
    }
    // =========================================================
    // CREATE USER ↔ ESTABLISHMENT RELATION
    // =========================================================
    @Transactional
    public void deleteUserStablishmentRelationsByStablishmentId(
        Long stablishmentId
    ) {
        String code = getStablishmentCodeById(stablishmentId);
        UsersPointsDto usersPointsDto = deleteAllUserPointsToDeleteStablishment(code);
        try {
            userStablishmentCRUDService.deleteByStablishmentId(stablishmentId);
        } catch (GeneralException e) {
            rollbackUserPointsDeleting(usersPointsDto);
            throw e;
        }
    }
        private UsersPointsDto deleteAllUserPointsToDeleteStablishment(
            String StablishmentCode
        ) {
            return webClientService.secureDeleteMethod(
                userPointsUrl + "/stablishments/{stablishmentCode}", 
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
        } catch(GeneralException e) {
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
                userPointsUrl + "/user/{userId}", 
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
    // DELETE EMPLOYEE RALATION
    // =========================================================
    @Transactional
    public void deleteEmployeeStablishmentRelations(
        Long userId
    ) {
        userStablishmentCRUDService.deleteByUsertId(userId);
    }
    // =========================================================
    // GET ALL USERS ID BY STABLISHMENT ID
    // =========================================================
    public List<Long> getAllUsersIdByStablishmentId(
        Long stablishmentId
    ) {
        return userStablishmentCRUDService.getAllIdsByStablishmentId(stablishmentId);
    }
    // =========================================================
    // GET RELATION BY USERS ID
    // =========================================================
    public UserStablishment getRelationByUserId(
        Long userId
    ) {
        return userStablishmentCRUDService.getByEmployeeId(userId);    
    }
    // =========================================================
    // GET EMPLOYEES
    // =========================================================
    public UsersRelationsResponseDto getAllEmployess(
        String token
    ) {
        token = jwtUtil.cleanJwtToken(token);
        String stablishmentCode = jwtUtil.getClaim(token, "establishmentCode", String.class);
        Long stablishmentId = getStablishmentIdByCode(stablishmentCode);
        List<UserStablishment> relations = userStablishmentCRUDService.getByStablishmentId(stablishmentId);
        relations.forEach(relation -> System.out.println(relation.getId()));
        return getUsersInfo(relations);
    }
        // Get info from auth service
        private UsersRelationsResponseDto getUsersInfo(
            List<UserStablishment> relations
        ){
            UsersIdsRequestDto ids = UsersIdsRequestDto.builder()
            .userIds(
                relations.stream().map(relation -> relation.getUserId()).toList()
            ) 
            .build();
            UsersDto users = webClientService.securePostMethod(
                authServiceUrl + "/employees/search",
                ids, 
                UsersDto.class);
            users.getUsers().forEach(user -> System.out.println(user.getUsername()+"--------------------"));
            List<RelationUserStablishmentDto> relationsList = users.getUsers().stream().map(user -> {
                RelationUserStablishmentDto relation = RelationUserStablishmentDto.builder()
                    .userId(user)
                    .registeredAt(relations.stream()
                        .filter(r -> r.getUserId().equals(user.getId()))
                        .findFirst()
                        .orElseThrow(() -> new GeneralException(StablishmentError.STABLISHMENT_NOT_FOUND))
                        .getRegisteredAt())
                    .build();
                return relation;
            }).toList();
            return UsersRelationsResponseDto.builder()
                .relations(relationsList)
                .build();
        }
    public UsersRelationsResponseDto getAllCustomers(
        String token
    ) {
        token = jwtUtil.cleanJwtToken(token);
        String stablishmentCode = jwtUtil.getClaim(token, "establishmentCode", String.class);
        Long stablishmentId = getStablishmentIdByCode(stablishmentCode);
        List<UserStablishment> relations = userStablishmentCRUDService.getByStablishmentId(stablishmentId);
        relations.forEach(relation -> System.out.println(relation.getId()));
        return getCustomerInfo(relations, stablishmentCode);
    }
        // Get info from auth service
        private UsersRelationsResponseDto getCustomerInfo(
            List<UserStablishment> relations,
            String code
        ){
            UsersIdsRequestDto ids = UsersIdsRequestDto.builder()
            .userIds(
                relations.stream().map(relation -> relation.getUserId()).toList()
            ) 
            .build();
            UsersDto users = webClientService.securePostMethod(
                authServiceUrl + "/customers/search",
                ids, 
                UsersDto.class);
            users.getUsers().forEach(user -> System.out.println(user.getUsername()+"--------------------"));
            List<RelationUserStablishmentDto> relationsList = users.getUsers().stream().map(user -> {
                RelationUserStablishmentDto relation = RelationUserStablishmentDto.builder()
                    .userId(user)
                    .registeredAt(relations.stream()
                        .filter(r -> r.getUserId().equals(user.getId()))
                        .findFirst()
                        .orElseThrow(() -> new GeneralException(StablishmentError.STABLISHMENT_NOT_FOUND))
                        .getRegisteredAt())
                    .build();
                return relation;
            }).toList();
            return  getInfoFromUserPointsService(
                UsersRelationsResponseDto.builder()
                .relations(relationsList)
                .build(),
                code
            );
        }
        private UsersRelationsResponseDto getInfoFromUserPointsService(
            UsersRelationsResponseDto relations,
            String code
        ) {
            List<Long> ids = relations.getRelations().stream().map(relation -> relation.getUserId().getId()).toList();
            UsersPointsDto wallets = webClientService.securePostMethod(
                userPointsUrl + "/stablishment/{code}/customers/wallets",
                UsersIdsRequestDto.builder().userIds(ids).build(),
                UsersPointsDto.class,
                code
            );
            relations.getRelations().forEach(
                relation -> 
                {
                    Long id = relation.getUserId().getId();

                    Integer balance = wallets.getUserPoints()
                            .stream()
                            .filter(wallet -> wallet.getUserId().equals(id))
                            .map(wallet -> wallet.getBalance())
                            .findFirst()
                            .orElse(0);
                    relation.setWallet(balance);
                
                }
            );
            return relations;
            
        }
    public UsersIdsRequestDto getAllUsers(
        String token
    ) {
        token = jwtUtil.cleanJwtToken(token);
        String stablishmentCode = jwtUtil.getClaim(token, "establishmentCode", String.class);
        Long stablishmentId = getStablishmentIdByCode(stablishmentCode);
        List<UserStablishment> relations = userStablishmentCRUDService.getByStablishmentId(stablishmentId);
        UsersIdsRequestDto ids = UsersIdsRequestDto.builder()
            .userIds(
                relations.stream().map(relation -> relation.getUserId()).toList()
            ) 
            .build();
        return ids;
    }
    // =========================================================
    // DELETE RELATION CUSTOMER
    // =========================================================
    public void deleteCustomerRelationByUserId(
        Long userId
    ) {
        int result = userStablishmentCRUDService.deleteByUsertId(userId);
        if (result > 0) deleteWallets(userId);
    }
        private UsersPointsDto deleteWallets(
            Long id
        ) {
            return webClientService.secureDeleteMethod(
                userPointsUrl + "/user/{userId}", 
                UsersPointsDto.class, 
                id);
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
        } catch(GeneralException e) {
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
                userPointsUrl + "/user/{userId}/stablishment/{stablishmentCode}", 
                UsersPointsDto.class, 
                userId,
                stablishmentCode
            );
        }
    // =========================================================
    // ROLLBACK DELETE STABLISHMENT
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