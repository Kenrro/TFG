package com.authservice.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.authservice.auth.entity.Role;
import com.authservice.auth.entity.User;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndRole(String username, Role admin);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id IN :ids")
    void deleteByIds(@Param("ids") List<Long> ids);

    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    List<User> findAllByIds(@Param("ids") List<Long> ids);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.id IN :ids AND u.role <> 'CUSTOMER'")
    void deleteByIdsEmployees(@Param("ids") List<Long> userIds);

    @Query("SELECT u FROM User u WHERE u.id IN :ids AND u.role <> 'CUSTOMER'")
    List<User> findAllEmployees(@Param("ids") List<Long> ids);

}
