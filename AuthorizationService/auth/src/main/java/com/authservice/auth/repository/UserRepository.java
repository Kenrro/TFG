package com.authservice.auth.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.authservice.auth.entity.Role;
import com.authservice.auth.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndRole(String username, Role admin);
}
