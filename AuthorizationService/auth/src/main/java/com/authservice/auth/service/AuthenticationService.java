package com.authservice.auth.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.authservice.auth.dto.auth.AuthLoginCustomerRequestDto;
import com.authservice.auth.dto.auth.AuthLoginEmployeeRequestDto;
import com.authservice.auth.dto.auth.AuthRegisterCustomerRequestDto;
import com.authservice.auth.dto.auth.AuthRegisterEmployeeRequestDTO;
import com.authservice.auth.dto.auth.AuthResponseDto;
import com.authservice.auth.dto.auth.AuthUpdateCustomerRequestDto;
import com.authservice.auth.entity.Role;
import com.authservice.auth.entity.User;
import com.authservice.auth.enums.AuthError;
import com.authservice.auth.exception.AuthException;
import com.authservice.auth.jwt.JwtUtil;
import com.authservice.auth.repository.UserRepository;


import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    
    public AuthResponseDto registerCustomer(AuthRegisterCustomerRequestDto param) {
        User user = User.builder()
            .username(param.getUsername())
            .password(passwordEncoder.encode(param.getPassword()))
            .name(param.getName())
            .lastname(param.getLastname())
            .role(Role.CUSTOMER)
            .build();
       try {
            user = userRepository.save(user);
            String token = jwtUtil.generateToken(user);
            return AuthResponseDto.builder()
                .token(token)
                .build();

       } catch (DataIntegrityViolationException e) {
              throw new AuthException(
                AuthError.ALREDY_EXIST_A_USER_WITH_THE_SAME_PHONE
              );
       }
       catch (ConstraintViolationException e) {
              throw new AuthException(
                AuthError.INVALID_USER_DATA
              );
       }
       catch (Exception e) { // lugo creo excepciones especificas
              throw new AuthException(
                AuthError.ERROR_CREATING_USER
              );
       }
    } 
    @Transactional
    public void updateCustomer(Long id, AuthUpdateCustomerRequestDto request) {
        User user = userRepository.findById(id)
        .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND));
        if (user.getRole() != null && user.getRole() != Role.CUSTOMER) {
            throw new AuthException(AuthError.INVALID_ROLE_UPDATE_CUSTOMER);
        }

        if (request.getUsername() != null
            && !request.getUsername().isBlank()
            && !request.getUsername().equals(user.getUsername())) {
            user.setUsername(request.getUsername());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getName() != null && !request.getName().isEmpty() && !request.getName().equals(user.getName())) {
            user.setName(request.getName());
        }
        if (request.getLastname() != null && !request.getLastname().isEmpty() && !request.getLastname().equals(user.getLastname())) {
            user.setLastname(request.getLastname());
        }
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
            User dbUser = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND));
            String token = jwtUtil.generateToken(dbUser);
            return AuthResponseDto.builder()
                .token(token)
                .build();
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
         try {
            User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND));
            userRepository.deleteById(user.getId());
        } catch (DataAccessException e) {
            throw new AuthException(AuthError.DATABASE_ERROR);
        }
    }
    // -------------------------

    // Register employee --------------------------------
    private String getEstablishmentCode() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User admin = (User) auth.getPrincipal();
        long adminId = admin.getId();
        System.out.println("Admin ID: " + adminId+"-------------------");
        // Lógica para obtener el código del establecimiento asociado al admin
        // find and return stablishment id
        return "sthm-4659";
    }
    private boolean isUserEmployed(long userId, String establishmentCode) {
        // Logica para verificar si el usuario ya está empleado en el establecimiento
        return true;
    }
    private boolean addToEmployeeEstablishment(User user, String establishmentCode) {
        // Lógica para asociar el empleado al establecimiento
        return true;
    }
    @Transactional
    public void registerEmployee(AuthRegisterEmployeeRequestDTO request) {
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .name(request.getName())
            .lastname(request.getLastname())
            .role(request.getRole())
            .build();
        String establishmentCode = getEstablishmentCode();
        if (establishmentCode == null) throw new RuntimeException("User is not admin");
        if (!addToEmployeeEstablishment(user, establishmentCode)) throw new AuthException(AuthError.ERROR_ASOSCIATING_EMPLOYEE_ESTABLISHMENT);
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AuthException(AuthError.ALREDY_EXIST_A_USER_WITH_THE_SAME_PHONE);
        } catch (ConstraintViolationException e) {
            throw new AuthException(AuthError.INVALID_USER_DATA);
        }
    }
    public void createEstablishmentAdmin(AuthRegisterEmployeeRequestDTO request) {
        User user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .name(request.getName())
            .lastname(request.getLastname())
            .role(Role.ADMIN)
            .build();
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AuthException(AuthError.ALREDY_EXIST_A_USER_WITH_THE_SAME_PHONE);
        } catch (ConstraintViolationException e) {
            throw new AuthException(AuthError.INVALID_USER_DATA);
        } 
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
            throw new AuthException(AuthError.INVALID_CREDENTIALS);
        } catch (DataAccessException e) {
            throw new AuthException(AuthError.DATABASE_ERROR);
        } 
        if (isUserEmployed(user.getId(), request.getEstablishmentCode()) == false) {
            // recibir el error del microservicio de establecimientos
            throw new RuntimeException("User is not employed in the establishment");
        }
        String token = jwtUtil.generateToken(user, request.getEstablishmentCode());
        return AuthResponseDto.builder()
            .token(token)
            .build();
    }
    @Transactional
    public void updateEmployee(Long id, AuthUpdateCustomerRequestDto request) {
        if (request.getRole() != null && request.getRole() != Role.ADMIN && request.getRole() != Role.SELLER) {
            throw new AuthException(AuthError.INVALID_ROLE_UPDATE_EMPLOYEE);
        }
        User user = userRepository.findById(id)
        .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND));

        if (request.getUsername() != null
            && !request.getUsername().isBlank()
            && !request.getUsername().equals(user.getUsername())) {
            user.setUsername(request.getUsername());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getName() != null && !request.getName().isEmpty() && !request.getName().equals(user.getName())) {
            user.setName(request.getName());
        }
        if (request.getLastname() != null && !request.getLastname().isEmpty() && !request.getLastname().equals(user.getLastname())) {
            user.setLastname(request.getLastname());
        }
        if (request.getRole() != null && request.getRole() != user.getRole()) {
            user.setRole(request.getRole());
        }
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AuthException(AuthError.ALREDY_EXIST_A_USER_WITH_THE_SAME_PHONE);
        } catch (ConstraintViolationException e) {
            throw new AuthException(AuthError.INVALID_USER_DATA);
        }
    }
    @Transactional
    public void deleteEmployee(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthException(AuthError.USER_NOT_FOUND);
        } catch (DataAccessException e) {
            throw new AuthException(AuthError.DATABASE_ERROR);
        }
    }
    

}
    
