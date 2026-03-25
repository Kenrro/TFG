package com.authservice.auth.service.authentication;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.authservice.auth.dto.auth.AuthCreateEstablishMentAdminResponseDto;
import com.authservice.auth.dto.auth.AuthLoginEmployeeRequestDto;
import com.authservice.auth.dto.auth.AuthRegisterEmployeeRequestDTO;
import com.authservice.auth.dto.auth.AuthResponseDto;
import com.authservice.auth.dto.auth.AuthUpdateCustomerRequestDto;
import com.authservice.auth.dto.auth.UserDto;
import com.authservice.auth.dto.auth.UsersDto;
import com.authservice.auth.dto.stablisment.CreateRelationUserWithStablishmentRequestDto;
import com.authservice.auth.dto.stablisment.RollbackDeleteEmployeesRequestDto;
import com.authservice.auth.dto.stablisment.UsersIdsRequestDto;
import com.authservice.auth.dto.stablisment.UsersQuantityResponseDto;
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
        // get stablishment code from stablishmentService
        private String getEstablishmentCode(Long id) {
            return webClientService.secureGetMethod(
                stablishmentMicroServiceUrl + "/{userId}/stablishment-code",
                String.class,
                id);
        }
   
    // =========================================================
    // Register Employee
    // =========================================================
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
                userStablishmentMicroServiceUrl + "/stablishments/employees",
                CreateRelationUserWithStablishmentRequestDto.builder()
                    .userId(user.getId())
                    .StablishmentCode(establishmentCode)
                    .build(),
                Void.class);
            // send dto and if error delete user created before

        } catch(GeneralException e){
            userService.deleteById(user.getId());
            throw e;
        } 
        catch(Exception e){
            e.printStackTrace();
            userService.deleteById(user.getId());
            throw new GeneralException(AuthError.ERROR_CREATING_USER);
        }
    }
        // Get stablishment code from userStablishmentService
        private String getEstablishmentCode() {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            String username;
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString(); // fallback
            }

            // find admin user in db
            User admin = userRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(AuthError.USER_NOT_FOUND));
            // get stablishment code from stablishment service
            long adminId = admin.getId();
            return getEstablishmentCode(adminId);
        }


        
       
    // =========================================================
    // Create stablishment admin
    // =========================================================
    @Transactional
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
    // =========================================================
    // LOGIN EMPLOYEE - ADMIN|SELLER
    // =========================================================
    @Transactional
    public AuthResponseDto loginEmployee(AuthLoginEmployeeRequestDto request) {
        User user;
        String code;
        try {
            Authentication auth = authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                )
            );
            user = (User) auth.getPrincipal();
            if (user.getRole() == Role.CUSTOMER) {
                throw new GeneralException(AuthError.ACCESS_DENIED);
            }
            code = getEstablishmentCode(user.getId());
            if (!code.equals(request.getEstablishmentCode())) {
                throw new GeneralException(AuthError.ACCESS_DENIED);
            }
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            e.printStackTrace();
            throw new GeneralException(AuthError.INVALID_CREDENTIALS);
        } catch (DataAccessException e) {
            throw new GeneralException(AuthError.DATABASE_ERROR);
        } 
        
        return generateTokenForUser(user, code);
    }
    // =========================================================
    // UPDATE EMPLOYEE # Just for admin
    // =========================================================
    @Transactional
    public void updateEmployee(Long id, AuthUpdateCustomerRequestDto request) {
        if (request.getRole() != null && request.getRole() != Role.ADMIN && request.getRole() != Role.SELLER) {
            throw new GeneralException(AuthError.INVALID_ROLE_UPDATE_EMPLOYEE);
        }
        User user = userService.findById(id);

        // map of field names to their corresponding getters in AuthUpdateCustomerRequestDto
        Map<String, Function<AuthUpdateCustomerRequestDto, Object>> fieldMap = Map.of(
        "password", req -> request.getPassword() != null ? passwordEncoder.encode(req.getPassword()) : null,
        "name", AuthUpdateCustomerRequestDto::getName,
        "lastname", AuthUpdateCustomerRequestDto::getLastname,
        "role", AuthUpdateCustomerRequestDto::getRole
        );

        copyNonNullProperties(request, user, fieldMap);

        userService.updateUser(user);
    }
    // =========================================================
    // DELETE A EMPLOYEE
    // =========================================================
    @Transactional
    public void deleteEmployee(Long id) { // Delete a employee and relations
        deleteAllRelationsFromStablishmentService(id); // First step, delete relations from stablishment service
        userService.deleteById(id); // delete user
    }
        private void deleteAllRelationsFromStablishmentService(
            Long id
        ) {
            webClientService.secureDeleteMethod(
                userStablishmentMicroServiceUrl + "/users/{userId}/stablishments", 
                Void.class, 
                id);
        }

    // =========================================================
    // DELETE ALL EMPLOYEES 
    // =========================================================
    @Transactional
    public UsersDto deleteEmployees(
        UsersIdsRequestDto request
    ) {
        List<UserDto> deletedUsers = userService.deleteEmployees(request).stream()
            .map(user -> UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .username(user.getUsername())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build())
            .toList();
        // return response
        return UsersDto.builder()
            .users(deletedUsers)
            .build();
    }
    // If delete all employees is failed, system do roll back
    public void rollbackDeleteEmployees(RollbackDeleteEmployeesRequestDto request) {
        List<User> users = request.getEmployees().stream().map(dto ->  
            User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .lastname(dto.getLastname())
                .username(dto.getUsername())
                .role(dto.getRole())
                .createdAt(dto.getCreatedAt())
                .build()   
        ).toList();
        userService.saveAll(users);
    }
    // =========================================================
    // GET ALL USERS
    // =========================================================
    public UsersDto getAllUsers(
        UsersIdsRequestDto request
    ) {
        List<UserDto> users = userService.findAllEmployees(request).stream().map(user ->
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
    public UsersQuantityResponseDto getUsersQuantity(
        UsersIdsRequestDto request
    ) {
        int employees = 0;
        int customers = 0;
        List<User> users = userService.findAllByIds(request);
        for(User user : users) {
            if (user.getRole() == Role.CUSTOMER) {
                customers++;
            } else {
                employees++;
            }
        }
        return UsersQuantityResponseDto.builder()
            .employees(employees)
            .customers(customers)
            .build();
    }

    public void changePassword(Long id, String newPassword) {
        User user = userService.findById(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user);
    }

}
    
