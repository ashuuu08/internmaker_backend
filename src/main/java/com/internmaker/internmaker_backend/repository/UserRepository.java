package com.internmaker.internmaker_backend.repository;

import java.util.Optional;

import com.internmaker.internmaker_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
