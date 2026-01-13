package com.stablishmentservice.stablishmentservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.stablishmentservice.stablishmentservice.dto.authorization.CreateStablishmentUserResponseDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.AdminUserRequestDto;
import com.stablishmentservice.stablishmentservice.dto.stablishment.StablishmentRequestDto;
import com.stablishmentservice.stablishmentservice.entity.Stablishment;
import com.stablishmentservice.stablishmentservice.repository.StablishmentRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StablishmentService {
    private final StablishmentRepository stablishmentRepository;
    private final UserStablishmentService userStablishmentService;

    @Value("${app.servicescredential.authServiceUrl}")
    private String authServiceUrl;

    @Qualifier("securedWebClient")
    private final WebClient securedWebClient;

    public List<Stablishment> getAllStablishments() {
        // Implementation to retrieve all stablishments
        return stablishmentRepository.findAll(); 
    }

    // saga patter
    @Transactional
    public void createStablishmentWithAdmin(StablishmentRequestDto stablishmentDto,  
        AdminUserRequestDto admin) {
            Stablishment stablishment = null;
            Long adminId = null;
            try{
                stablishment = stablishmentRepository.save(
                    Stablishment.builder()
                .name(stablishmentDto.getName())
                .address(stablishmentDto.getAddress())
                .description(stablishmentDto.getDescription())
                .code(stablishmentDto.getCode())
                .build()
            );
            
            CreateStablishmentUserResponseDto response = createAdminInAuthService(admin);
            
            adminId = response.getAdminId();
            
            // Crear relación entre usuario y establecimiento
            userStablishmentService.createUserStablishmentRelation(
                adminId, 
                stablishment.getId()
            );
        } catch (Exception e) {
            // Manejar la compensación en caso de error
            if (adminId != null) {
                // Llamar al servicio de autenticación para eliminar el usuario creado
                rollBackAdminCreation(adminId);
            }
            if (stablishment != null) {
                // Eliminar el establecimiento creado
                stablishmentRepository.deleteById(stablishment.getId());
            }
            throw new RuntimeException("Error creating stablishment with admin", e);
        }
    }
    private CreateStablishmentUserResponseDto createAdminInAuthService(
        AdminUserRequestDto admin) {
    
        return securedWebClient
            .post()
            .uri(authServiceUrl + "/create-establishment-admin")
            .bodyValue(admin)
            .exchangeToMono(response -> {
                if (response.statusCode().equals(HttpStatus.CREATED)) {
                    return response.bodyToMono(CreateStablishmentUserResponseDto.class);
                }
                return response.createException().flatMap(Mono::error);
            })
            .block();
    }
    private void rollBackAdminCreation(Long adminId) {
        securedWebClient
            .delete()
            .uri(authServiceUrl + "/delete-employee/" + adminId)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
