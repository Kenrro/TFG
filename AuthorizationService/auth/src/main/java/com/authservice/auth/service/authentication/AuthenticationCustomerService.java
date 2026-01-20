package com.authservice.auth.service.authentication;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authservice.auth.dto.auth.AuthLoginCustomerRequestDto;
import com.authservice.auth.dto.auth.AuthRegisterCustomerRequestDto;
import com.authservice.auth.dto.auth.AuthResponseDto;
import com.authservice.auth.dto.auth.AuthUpdateCustomerRequestDto;
import com.authservice.auth.entity.Role;
import com.authservice.auth.entity.User;
import com.authservice.auth.enums.AuthError;
import com.authservice.auth.exception.AuthException;
import com.authservice.auth.jwt.JwtUtil;
import com.authservice.auth.repository.UserRepository;
import com.authservice.auth.service.UserService;

import java.lang.reflect.Field;
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
    public AuthResponseDto registerCustomer(AuthRegisterCustomerRequestDto param) {
        User user = User.builder()
            .username(param.getUsername())
            .password(passwordEncoder.encode(param.getPassword()))
            .name(param.getName())
            .lastname(param.getLastname())
            .role(Role.CUSTOMER)
            .build();
        user = userService.createUser(user);
        return generateTokenForUser(user, null);
    } 
    @Transactional
    public void updateCustomer(Long id, AuthUpdateCustomerRequestDto request) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND));
        if (user.getRole() != null && user.getRole() != Role.CUSTOMER) {
            throw new AuthException(AuthError.INVALID_ROLE_UPDATE_CUSTOMER);
        }
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
    // LOGIN CUSTOMER --------------------------------
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

            return generateTokenForUser(dbUser, null);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new AuthException(AuthError.INVALID_CREDENTIALS);
        } catch (DataAccessException e) {
            throw new AuthException(AuthError.DATABASE_ERROR);
        } catch (Exception e) {
            throw new AuthException(AuthError.ERROR_LOGIN_CUSTOMER);
        }
    }
    // Delete customer ------------------------------
    @Transactional
    public void deleteCustomer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.deleteUser(
            userService.findByUsername(username).getId()
        );
    }
    public Long getCustomerIdByUsername(String username) {
        return userService.findByUsername(username).getId();
    }
    
}
    
