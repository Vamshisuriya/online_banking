package com.example.onlinebanking.repository;

import com.example.onlinebanking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByCustomerId(String customerId);
    Optional<User> findByCustomerId(String customerId);
    Optional<User> findByEmail(String email);
}
