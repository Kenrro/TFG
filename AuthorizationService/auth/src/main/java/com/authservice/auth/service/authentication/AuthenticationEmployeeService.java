package com.authservice.auth.service.authentication;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.authservice.auth.dto.auth.AuthCreateEstablishMentAdminResponseDto;
import com.authservice.auth.dto.auth.AuthLoginEmployeeRequestDto;
import com.authservice.auth.dto.auth.AuthRegisterEmployeeRequestDTO;
import com.authservice.auth.dto.auth.AuthResponseDto;
import com.authservice.auth.dto.auth.AuthUpdateCustomerRequestDto;
import com.authservice.auth.dto.stablisment.CreateRelationUserWithStablishmentRequestDto;
import com.authservice.auth.dto.stablisment.DeleteUsersInAuthServiceRequestDto;
import com.authservice.auth.dto.stablisment.DeleteUsersInAuthServiceResponseDto;
import com.authservice.auth.dto.stablisment.RollbackDeleteEmployeesRequestDto;
import com.authservice.auth.entity.Role;
import com.authservice.auth.entity.User;
import com.authservice.auth.enums.AuthError;
import com.authservice.auth.exception.AuthException;
import com.authservice.auth.jwt.JwtUtil;
import com.authservice.auth.repository.UserRepository;
import com.authservice.auth.service.UserService;
import com.authservice.auth.service.WebClientService;


import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationEmployeeService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final WebClientService webClientService;
    private final UserService userService;
    @Qualifier("securedWebClient") private final WebClient securedWebClient;
    @Value("${app.microservices.establishment-service-userstablishment-url}")
    private String userStablishmentMicroServiceUrl;
    @Value("${app.microservices.establishment-service-stablishment-url}")
    private String stablishmentMicroServiceUrl;

        // Generate token for user with optional establishment code
        private AuthResponseDto generateTokenForUser(User user, String establishmentCode) {
            String token;
            if (establishmentCode != null) {
                token = jwtUtil.generateToken(user, establishmentCode);
            } else {
                token = jwtUtil.generateToken(user);
            }
            return AuthResponseDto.builder()
                    .token(token)
                    .build();
        }
        public static <T, R> void copyNonNullProperties(T source, R target, Map<String, Function<T, Object>> fieldMappers) {
            fieldMappers.forEach((fieldName, getter) -> {
                Object value = getter.apply(source);
                if (value != null) {
                    try {
                        Field targetField = target.getClass().getDeclaredField(fieldName);
                        targetField.setAccessible(true);
                        targetField.set(target, value);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException("Failed to set field " + fieldName, e);
                    }
                }
            });
        }
   
    // Register employee --------------------------------
    @Transactional
    public void registerEmployee(AuthRegisterEmployeeRequestDTO request) {
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .name(request.getName())
            .lastname(request.getLastname())
            .role(request.getRole())
            .build();
            userService.createUser(user);

        try{
            String establishmentCode = getEstablishmentCode();
            // call to establishment service to create relation user-establishment from webClientService
            webClientService.securePostMethod(
                userStablishmentMicroServiceUrl + "/add-relation-user-stablishment",
                CreateRelationUserWithStablishmentRequestDto.builder()
                    .userId(user.getId())
                    .StablishmentCode(establishmentCode)
                    .build(),
                Void.class);
            // send dto and if error delete user created before

        } catch(AuthException e){
            userService.deleteUser(user.getId());
            throw e;
        } 
        catch(Exception e){
            e.printStackTrace();
            userService.deleteUser(user.getId());
            throw new AuthException(AuthError.ERROR_CREATING_USER);
        }
    }
        private String getEstablishmentCode() {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString(); // fallback
            }

            // Luego buscar tu User en DB
            User admin = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND));

            long adminId = admin.getId();
            return webClientService.secureGetMethod(
                stablishmentMicroServiceUrl + "/get-stablishment-code/{userId}",
                String.class,
                adminId);
        }
       

    public AuthCreateEstablishMentAdminResponseDto createEstablishmentAdmin(AuthRegisterEmployeeRequestDTO request) {
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .name(request.getName())
            .lastname(request.getLastname())
            .role(Role.ADMIN)
            .build();
        userService.createUser(user);
        return AuthCreateEstablishMentAdminResponseDto.builder()
                .adminId(user.getId().toString())
                .build();
    }
    public AuthResponseDto loginEmployee(AuthLoginEmployeeRequestDto request) {
        User user;
        try {
            Authentication auth = authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
            user = (User) auth.getPrincipal();
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            e.printStackTrace();
            throw new AuthException(AuthError.INVALID_CREDENTIALS);
        } catch (DataAccessException e) {
            throw new AuthException(AuthError.DATABASE_ERROR);
        } 
        
        return generateTokenForUser(user, request.getEstablishmentCode());
    }

    @Transactional
    public void updateEmployee(Long id, AuthUpdateCustomerRequestDto request) {
        if (request.getRole() != null && request.getRole() != Role.ADMIN && request.getRole() != Role.SELLER) {
            throw new AuthException(AuthError.INVALID_ROLE_UPDATE_EMPLOYEE);
        }
        User user = userService.findById(id);

        // map of field names to their corresponding getters in AuthUpdateCustomerRequestDto
        Map<String, Function<AuthUpdateCustomerRequestDto, Object>> fieldMap = Map.of(
        "username", AuthUpdateCustomerRequestDto::getUsername,
        "password", req -> request.getPassword() != null ? passwordEncoder.encode(req.getPassword()) : null,
        "name", AuthUpdateCustomerRequestDto::getName,
        "lastname", AuthUpdateCustomerRequestDto::getLastname
        );

        copyNonNullProperties(request, user, fieldMap);

        userService.updateUser(user);
    }
    @Transactional
    public void deleteEmployee(Long id) {
        userService.deleteUser(id);
    }
    @Transactional
    public DeleteUsersInAuthServiceResponseDto deleteEmployees(DeleteUsersInAuthServiceRequestDto request) {
        return userService.deleteEmployees(request);
    }
    public void rollbackDeleteEmployees(RollbackDeleteEmployeesRequestDto request) {
        List<User> users = request.getEmployees().stream().map(dto ->  
            User.builder()
                .name(dto.getName())
                .lastname(dto.getLastname())
                .username(dto.getUsername())
                .role(dto.getRole())
                .createdAt(dto.getCreatedAt())
                .build()   
        ).toList();
        userService.saveAll(users);
    }

}
    
