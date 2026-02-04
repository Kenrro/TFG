package com.stablishmentservice.stablishmentservice.service.stablishment;

import java.util.List;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stablishmentservice.stablishmentservice.dto.authorization.CreateStablishmentUserResponseDto;
import com.stablishmentservice.stablishmentservice.dto.authorization.UsersIdsRequestDto;
import com.stablishmentservice.stablishmentservice.dto.authorization.DeleteUsersResponseDto;
import com.stablishmentservice.stablishmentservice.dto.authorization.UsersDto;
import com.stablishmentservice.stablishmentservice.dto.incentive.configuration.StablishmentConfigurationCreateRequestDto;
import com.stablishmentservice.stablishmentservice.dto.incentive.configuration.StablishmentConfigurationResponseDto;
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
    @Value("${app.servicescredential.incentive-service-stablishment-configuration-url}")
    private String configurationServiceUrl;

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
    // SAGA PATTERN - CREATE STABLISHMENT AND ADMIN
    // =========================================================
    // 
    @Transactional
    public StablishmentAndAdminResponseDto createStablishmentWithAdmin(StablishmentRequestDto stablishmentDto,  
        AdminUserRequestDto admin) {
            String code = GeneratedRamdonCode.generateUniqueCode(stablishmentDto.getName()); // generate the code with the letters
            Stablishment stablishment = null;
            Long adminId = null;
            StablishmentConfigurationResponseDto configuration = null;
        // 1. create stablishment
        stablishment = stablishmentCRUDService.create(Stablishment.builder()
        .name(stablishmentDto.getName())
        .address(stablishmentDto.getAddress())
        .description(stablishmentDto.getDescription())
        .code(code)
        .build());
        
        try {
            // 2. Call to auth service for crete admin with admin role
            admin.setRole("ADMIN");
            CreateStablishmentUserResponseDto response = webClientService.securePostMethod(
                authServiceUrl + "/create-establishment-admin",
                admin, 
                CreateStablishmentUserResponseDto.class);           
            adminId = response.getAdminId(); // get user id
            // 3. Create configuration in incentive service
            StablishmentConfigurationCreateRequestDto request = StablishmentConfigurationCreateRequestDto.builder()
            .stablishmentCode(code)
            .pointsPerEuro(10)
            .build();
            configuration = webClientService.securePostMethod(
                configurationServiceUrl, 
                request, 
                StablishmentConfigurationResponseDto.class);
            // 4. create relationship between user and stablishment
            userStablishmentService.createUserEmployeeStablishmentRelation(
                adminId, 
                stablishment.getCode()
            );
        } catch (Exception e) {
            e.printStackTrace();
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

            if (configuration != null) {
                // Delete configuration
                webClientService.secureDeleteMethod(
                    configurationServiceUrl + "/{stablishmentCode}", 
                    Void.class, 
                    code);
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
    public void updateStablishment(StablishmentRequestDto stablishmentDto, String token) {
        token = jwtUtil.cleanJwtToken(token);
        String code = jwtUtil.getClaim(token, "establishmentCode", String.class);
        stablishmentCRUDService.update(code, stablishmentDto);
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
        DeletedProductsResponseDto deletedProducts = null;
        UsersDto deleteUsersResponseDto = null;
        // get stablishment id
        Long stablishmentId = stablishmentCRUDService.findByCode(stablishmentCode).getId();
        List<Long> usersIds = userStablishmentService.getAllUsersIdByStablishmentId(stablishmentId);
        try {
            // Remove all users who are registered at the establishment
            deleteUsersResponseDto = webClientService.securePostMethod(authServiceUrl + "/delete-employees", 
                UsersIdsRequestDto.builder()
                    .userIds(usersIds)
                    .build(),
                    UsersDto.class);
            // Remove all productos from products service
            deletedProducts = webClientService.secureDeleteMethod(
                productServiceUrl + "/delete-by-code/{code}",
                DeletedProductsResponseDto.class ,
                stablishmentCode);
            webClientService.secureDeleteMethod(
                        configurationServiceUrl + "/{stablishmentCode}", 
                        Void.class, 
                        stablishmentCode);

        
            userStablishmentService.deleteUserStablishmentRelationsByStablishmentId(stablishmentId);
            stablishmentCRUDService.delete(stablishmentId);
        } catch (Exception e) {
            try {
                // rollback deleted user
                if (deleteUsersResponseDto != null) {
                    webClientService.securePostMethod(authServiceUrl + "/rollback-delete-employees",
                        deleteUsersResponseDto, 
                        Void.class);
                }
                // rollback deleted relations
                if (deleteUsersResponseDto != null) {
                    userStablishmentService.rollbackDeleteStablishment(
                    deleteUsersResponseDto.getUsers().stream().map(relation -> 
                        UserStablishment.builder()
                        .userId(relation.getId())
                        .stablishmentId(stablishmentId)
                        .build()      
                    )
                    .toList()
                    );
                }
                // rollback deleted products
                if (deletedProducts != null) {    
                
                    webClientService.securePostMethod(productServiceUrl + "/rollback-deleted-products", 
                    deletedProducts, 
                    Void.class);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            throw new StablishmentGeneralException(
                StablishmentError.STABLISHMENT_DELETION_FAILED
            );
        }
        
    }
    // =========================================================
    // GET STABLISHMENTS BY TOKEN
    // =========================================================
    // Obtain all the establishments where the user is registered
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
