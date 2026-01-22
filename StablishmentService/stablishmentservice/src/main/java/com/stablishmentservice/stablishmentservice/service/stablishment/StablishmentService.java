package com.stablishmentservice.stablishmentservice.service.stablishment;

import java.util.List;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stablishmentservice.stablishmentservice.dto.authorization.CreateStablishmentUserResponseDto;
import com.stablishmentservice.stablishmentservice.dto.authorization.DeleteUsersInAuthServiceRequestDto;
import com.stablishmentservice.stablishmentservice.dto.authorization.DeleteUsersResponseDto;
import com.stablishmentservice.stablishmentservice.dto.products.DeletedProductsResponseDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.AdminUserRequestDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentAndAdminResponseDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentRequestDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentResponseDto;
import com.stablishmentservice.stablishmentservice.entity.Stablishment;
import com.stablishmentservice.stablishmentservice.entity.UserStablishment;
import com.stablishmentservice.stablishmentservice.enums.StablishmentError;
import com.stablishmentservice.stablishmentservice.exception.StablishmentGeneralException;
import com.stablishmentservice.stablishmentservice.jwt.JwtUtil;
import com.stablishmentservice.stablishmentservice.service.GeneratedRamdonCode;
import com.stablishmentservice.stablishmentservice.service.WebClientService;
import com.stablishmentservice.stablishmentservice.service.userStablishment.UserStablishmentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StablishmentService {
    private final StablishmentCRUDService stablishmentCRUDService;
    private final UserStablishmentService userStablishmentService;
    private final GeneratedRamdonCode GeneratedRamdonCode;
    private final WebClientService webClientService;
    private final JwtUtil jwtUtil;

    @Value("${app.servicescredential.authServiceUrl}")
    private String authServiceUrl;
    @Value("${app.servicescredential.productServiceUrl}")
    private String productServiceUrl;

    // =========================================================
    // GET METHODS
    // =========================================================
    public List<Stablishment> getAllStablishments() {
        // Implementation to retrieve all stablishments
        return stablishmentCRUDService.findAll(); 
    }

    public StablishmentResponseDto getStablishmentByCode(String code) {
        Stablishment stablishment = stablishmentCRUDService.findByCode(code);
        return StablishmentResponseDto.builder()
            .code(stablishment.getCode())
            .address(stablishment.getAddress())
            .description(stablishment.getDescription())
            .name(stablishment.getName())
            .build();
    }
    public Stablishment getStablishmentById(Long id) {
        return stablishmentCRUDService.findById(id);
    }
    // Get stablishment code by user id
    public String getStablishmentCodeByUserId(Long userId) {
        Long stablishmentId = userStablishmentService.getRelationByUserId(userId).getStablishmentId();
        return stablishmentCRUDService.findById(stablishmentId)
            .getCode();
    }
    // =========================================================
    // SAGA PATTERN
    // =========================================================
    // 
    @Transactional
    public StablishmentAndAdminResponseDto createStablishmentWithAdmin(StablishmentRequestDto stablishmentDto,  
        AdminUserRequestDto admin) {
            String code = GeneratedRamdonCode.generateUniqueCode(stablishmentDto.getName());
            Stablishment stablishment = null;
            Long adminId = null;
        
        stablishment = stablishmentCRUDService.create(Stablishment.builder()
        .name(stablishmentDto.getName())
        .address(stablishmentDto.getAddress())
        .description(stablishmentDto.getDescription())
        .code(code)
        .build());
        
        try {
            CreateStablishmentUserResponseDto response = webClientService.securePostMethod(
                authServiceUrl + "/create-establishment-admin",
                admin, 
                CreateStablishmentUserResponseDto.class);           
            adminId = response.getAdminId();

            // Crear relación entre usuario y establecimiento
            userStablishmentService.createUserStablishmentRelation(
                adminId, 
                stablishment.getCode()
            );
        } catch (Exception e) {
            if (adminId != null) {
                // rollback created user in authservice
                webClientService.secureDeleteMethod(
                    authServiceUrl + "/delete-employee/" + adminId, 
                    Void.class, 
                    admin);
            }
            if (stablishment != null) {
                // Delete created stablishment
                stablishmentCRUDService.delete(stablishment.getId());
            }
            throw new StablishmentGeneralException(StablishmentError.STABLISHMENT_CREATION_FAILED);
        }

        return StablishmentAndAdminResponseDto.builder()
            .name(stablishment.getName())
            .description(stablishment.getDescription())
            .address(stablishment.getAddress())
            .code(stablishment.getCode())
            .build();
        
    }

    @Transactional
    public void updateStablishment(StablishmentRequestDto stablishmentDto, Long id) {
        stablishmentCRUDService.update(id, stablishmentDto);
        
    }
    // =========================================================
    // DELETE ESTABLISHMENT WITH SAGA PATTERN
    // =========================================================
    //1. get all users ids related to stablishment
    //2. notify auth service to delete users
    //3. if success, delete stablishment and relations
    @Transactional
    public void deleteStablishment(String token) {
        // get user token
        token = jwtUtil.cleanJwtToken(token);
        String stablishmentCode = jwtUtil.getClaim(token, "establishmentCode", String.class);

        // get stablishment id
        Long stablishmentId = stablishmentCRUDService.findByCode(stablishmentCode).getId();
        List<Long> usersIds = userStablishmentService.getAllUsersIdByStablishmentId(stablishmentId);
        // Remove all users who are registered at the establishment
        DeleteUsersResponseDto deleteUsersResponseDto = webClientService.securePostMethod(authServiceUrl + "/delete-employees", 
            DeleteUsersInAuthServiceRequestDto.builder()
                .userIds(usersIds)
                .build(),
                DeleteUsersResponseDto.class);
        // Remove all productos from products service
        DeletedProductsResponseDto deletedProducts = webClientService.secureDeleteMethod(
            productServiceUrl + "/delete-by-code/{code}",
            DeletedProductsResponseDto.class ,
            stablishmentCode);

        try {
            stablishmentCRUDService.delete(stablishmentId);
            userStablishmentService.deleteUserStablishmentRelationsByStablishmentId(stablishmentId);
        } catch (Exception e) {
            try {
                // rollback deleted user
                webClientService.securePostMethod(authServiceUrl + "/rollback-delete-employees",
                    deleteUsersResponseDto, 
                    Void.class);
                // rollback deleted relations
                userStablishmentService.rollbackDeleteStablishment(
                    deleteUsersResponseDto.getDeletedUsers().stream().map(relation -> 
                        UserStablishment.builder()
                        .userId(relation.getId())
                        .stablishmentId(stablishmentId)
                        .build()      
                    )
                    .toList()
                );
                // rollback deleted products
                webClientService.securePostMethod(productServiceUrl + "/rollback-deleted-products", 
                deletedProducts, 
                Void.class);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new StablishmentGeneralException(
                StablishmentError.STABLISHMENT_DELETION_FAILED
            );
        }
        
    }
    // =========================================================
    // GET STABLISHMENT BY TOKEN
    // =========================================================
    // is for the customers
    public List<StablishmentResponseDto> getStablishmentsByToken(String token) {
        token = jwtUtil.cleanJwtToken(token);
        Long userId = jwtUtil.getClaim(token, "id", Long.class);
        List<Long> stablishmentsId = userStablishmentService.getStablishmentIdByUserId(userId);
        return stablishmentCRUDService.findByIds(stablishmentsId).stream().map(stablishment ->
            StablishmentResponseDto.builder()
            .code(stablishment.getCode())
            .name(stablishment.getName())
            .description(stablishment.getDescription())
            .address(stablishment.getAddress())
            .build()   
        ).toList();

    }
        
}
