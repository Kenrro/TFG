package com.stablishmentservice.stablishmentservice.service.userStablishment;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.stablishmentservice.stablishmentservice.entity.UserStablishment;
import com.stablishmentservice.stablishmentservice.enums.UserStablishmentError;
import com.stablishmentservice.stablishmentservice.exception.StablishmentGeneralException;
import com.stablishmentservice.stablishmentservice.repository.UserStablishmentRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

// CRUD responsability
@Service
@RequiredArgsConstructor
public class UserStablishmentCRUDService {
    private final UserStablishmentRepository userStablishmentRepository;

    public UserStablishment getByEmployeeId(Long userId) {
        return userStablishmentRepository.findByEmployeeId(userId).orElseThrow(()->
        new StablishmentGeneralException(UserStablishmentError.USER_STABLISHMENT_RELATION_NOT_FOUND));
    }
    public List<UserStablishment> getByUserId(Long userId) {
        return userStablishmentRepository.findByUserId(userId);
    }
    public List<UserStablishment> getByStablishmentId(Long stablishmentId) {
        return userStablishmentRepository.findByStablishmentId(stablishmentId);
    }
    // get users id
    public List<Long> getAllIdsByStablishmentId(Long stablishmentId) {
        return userStablishmentRepository.findAllUsersIdByStablishmentId(stablishmentId);
    }
    
    // remove all users
    @Transactional
    public void deleteByStablishmentId(Long stablishmentId) {
        try {
            int deleted = userStablishmentRepository.deleteByStablishmentId(stablishmentId);

            if (deleted == 0) {
                throw new StablishmentGeneralException(
                    UserStablishmentError.USER_STABLISHMENT_RELATION_NOT_FOUND
                );
            }

        } catch (DataIntegrityViolationException e) {
            throw new StablishmentGeneralException(
                UserStablishmentError.USER_STABLISHMENT_RELATION_DELETION_FAILED);

        } catch (StablishmentGeneralException e) {
            throw e; // no la envuelvas de nuevo

        } catch (Exception e) {
            throw new StablishmentGeneralException(
                UserStablishmentError.USER_STABLISHMENT_RELATION_DELETION_FAILED);
        }
    }
    @Transactional
    public void deleteByUsertId(Long userId) {
        try {
            int deleted = userStablishmentRepository.deleteByUserId(userId);

            if (deleted == 0) {
                throw new StablishmentGeneralException(
                    UserStablishmentError.USER_STABLISHMENT_RELATION_NOT_FOUND
                );
            }

        } catch (DataIntegrityViolationException e) {
            throw new StablishmentGeneralException(
                UserStablishmentError.USER_STABLISHMENT_RELATION_DELETION_FAILED);

        } catch (StablishmentGeneralException e) {
            throw e;

        } catch (Exception e) {
            throw new StablishmentGeneralException(
                UserStablishmentError.USER_STABLISHMENT_RELATION_DELETION_FAILED);
        }
    }
    @Transactional
    public void deleteByUsertIdAndStablishmentId(Long userId, Long stablishmentId) {
        try {
            int deleted = userStablishmentRepository.deleteByUsertIdAndStablishmentId(userId, stablishmentId);

            if (deleted == 0) {
                throw new StablishmentGeneralException(
                    UserStablishmentError.USER_STABLISHMENT_RELATION_NOT_FOUND
                );
            }

        } catch (DataIntegrityViolationException e) {
            throw new StablishmentGeneralException(
                UserStablishmentError.USER_STABLISHMENT_RELATION_DELETION_FAILED);

        } catch (StablishmentGeneralException e) {
            throw e;

        } catch (Exception e) {
            throw new StablishmentGeneralException(
                UserStablishmentError.USER_STABLISHMENT_RELATION_DELETION_FAILED);
        }
    }
    @Transactional
    public void create(Long userId, Long stablishmentId) {
        try {
            UserStablishment userStablishment = UserStablishment.builder()
                .userId(userId)
                .stablishmentId(stablishmentId)
                .build();
            userStablishmentRepository.save(userStablishment);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new StablishmentGeneralException(UserStablishmentError.INVALID_USER_STABLISHMENT_DATA);
        } catch (ConstraintViolationException e) {
            throw new StablishmentGeneralException(UserStablishmentError.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public void saveAll(List<UserStablishment> relations) {
        try {
            userStablishmentRepository.saveAll(relations);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
