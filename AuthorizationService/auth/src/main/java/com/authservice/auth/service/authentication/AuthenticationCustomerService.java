package com.authservice.auth.service.authentication;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authservice.auth.dto.auth.AuthLoginCustomerRequestDto;
import com.authservice.auth.dto.auth.AuthRegisterCustomerRequestDto;
import com.authservice.auth.dto.auth.AuthResponseDto;
import com.authservice.auth.dto.auth.AuthUpdateCustomerRequestDto;
import com.authservice.auth.dto.auth.UserDto;
import com.authservice.auth.dto.auth.UsersDto;
import com.authservice.auth.dto.stablisment.UsersIdsRequestDto;
import com.authservice.auth.entity.Role;
import com.authservice.auth.entity.User;
import com.authservice.auth.enums.AuthError;
import com.authservice.auth.exception.GeneralException;
import com.authservice.auth.jwt.JwtUtil;
import com.authservice.auth.repository.UserRepository;
import com.authservice.auth.service.UserService;
import com.authservice.auth.service.WebClientService;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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
public class AuthenticationCustomerService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final WebClientService webClientService;
    @Value("${app.microservices.establishment-service-userstablishment-url}")
    private String userStablishmentMicroServiceUrl;
    @Value("${app.microservices.establishment-service-stablishment-url}")
    private String stablishmentMicroServiceUrl;
    @Value("${app.microservices.incentives-service-url}")
    private String incentiveService;

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
    // =========================================================
    // REGISTER CUSTOMER
    // =========================================================
    public AuthResponseDto registerCustomer(AuthRegisterCustomerRequestDto param) {
        User user = User.builder()
            .username(param.getUsername())
            .password(passwordEncoder.encode(param.getPassword()))
            .name(param.getName())
            .lastname(param.getLastname())
            .role(Role.CUSTOMER)
            .build();
        
        try {
            
            user = userService.createUser(user);
            // TODO: las carteras se crean al unir al usuario al negocio
        } catch(GeneralException e) {
            throw e;
        }
        return generateTokenForUser(user, null);
    } 
    // =========================================================
    // UPDATE CUSTOMER
    // =========================================================
    @Transactional
    public void updateCustomer(String token, AuthUpdateCustomerRequestDto request) {

        token = jwtUtil.cleanJwtToken(token);
        Long id = jwtUtil.getClaim(token, "id", Long.class);

        User user = userRepository.findById(id)
            .orElseThrow(() -> new GeneralException(AuthError.USER_NOT_FOUND));
        
        // Password (SIEMPRE encode)
        if (request.getPassword() != null) {
            if (request.getPassword().isBlank()) {
                throw new GeneralException(AuthError.INVALID_PASSWORD);
            }
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Name
        if (request.getName() != null) {
            user.setName(request.getName());
        }

        // Lastname
        if (request.getLastname() != null) {
            user.setLastname(request.getLastname());
        }

        userService.updateUser(user);
    }
    // =========================================================
    // LOGIN CUSTOMER
    // =========================================================
    public AuthResponseDto loginCustomer(AuthLoginCustomerRequestDto requestDto) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    requestDto.getUsername(),
                    requestDto.getPassword()
                )
            );
            UserDetails user = (UserDetails) auth.getPrincipal();
            User dbUser = userService.findByUsername(user.getUsername());
            if (dbUser.getRole() != Role.CUSTOMER) {
                throw new GeneralException(AuthError.ACCESS_DENIED);
                
            }
            return generateTokenForUser(dbUser, null);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new GeneralException(AuthError.INVALID_CREDENTIALS);
        } catch (DataAccessException e) {
            throw new GeneralException(AuthError.DATABASE_ERROR);
        } catch (Exception e) {
            throw new GeneralException(AuthError.ERROR_LOGIN_CUSTOMER);
        }
    }
    // =========================================================
    // DELETE CUSTOMER
    // =========================================================
    @Transactional
    public void deleteCustomer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Long id = userService.findByUsername(username).getId();
        deleteAllRelationsFromStablishmentService(id);
        userService.deleteById(id);
    }
    private void deleteAllRelationsFromStablishmentService(
        Long id
    ) {
        webClientService.secureDeleteMethod(
            userStablishmentMicroServiceUrl + "/users/{userId}/stablishments", 
            Void.class, 
            id);
        }
        public Long getCustomerIdByUsername(String username) {
            return userService.findByUsername(username).getId();
        }
        
    // =========================================================
    // DELETE CUSTOMER
    // =========================================================
    public UsersDto getAllUsers(
        UsersIdsRequestDto request
    ) {
        List<UserDto> users = userService.findAllCustomers(request).stream().map(user ->
            UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build())
            .toList();
        return UsersDto.builder()
        .users(users)
        .build();
    }
    }
    
