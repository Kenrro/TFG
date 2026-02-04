package com.authservice.auth.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.authservice.auth.dto.stablisment.UsersIdsRequestDto;
import com.authservice.auth.entity.User;
import com.authservice.auth.enums.AuthError;
import com.authservice.auth.exception.AuthException;
import com.authservice.auth.repository.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AuthException(AuthError.ALREDY_EXIST_A_USER_WITH_THE_SAME_PHONE);
        } catch (ConstraintViolationException e) {
            throw new AuthException(AuthError.INVALID_USER_DATA);
        } catch (DataAccessException e) {
            throw new AuthException(AuthError.DATABASE_ERROR);
        }
    }

    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND));
    }

    public void updateUser(User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new AuthException(AuthError.ALREDY_EXIST_A_USER_WITH_THE_SAME_PHONE);
        } catch (ConstraintViolationException e) {
            throw new AuthException(AuthError.INVALID_USER_DATA);
        }
    }

    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND));
        try {
            userRepository.delete(user);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthException(AuthError.USER_NOT_FOUND);
        } catch (DataAccessException e) {
            throw new AuthException(AuthError.DATABASE_ERROR);
        }
    }
    public void deleteById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new AuthException(AuthError.USER_NOT_FOUND));
        try {
            userRepository.delete(user);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthException(AuthError.USER_NOT_FOUND);
        } catch (DataAccessException e) {
            throw new AuthException(AuthError.DATABASE_ERROR);
        }
    }
    @Transactional
    public List<User> deleteEmployees(
        UsersIdsRequestDto request
    ) {
        // get users
        List<User> users = userRepository.findAllByIds(request.getUserIds());

       
        try {
            userRepository.deleteByIdsEmployees(request.getUserIds());
        } catch (DataAccessException e) {
            throw new AuthException(AuthError.DATABASE_ERROR);
        }

        // return response
        return users;
    }
    public List<User> findAllEmployees(
        UsersIdsRequestDto request
    ) {
        return userRepository.findAllEmployees(request.getUserIds());
    }

    public void saveAll(List<User> users) {
        try {
            userRepository.saveAll(users);
        } catch (DataAccessException e) {
            throw new AuthException(AuthError.DATABASE_ERROR);
        }
    }
}
