package com.nutritrack.repository;

import com.nutritrack.entity.RefreshToken;
import com.nutritrack.entity.User; // Import User
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user); // Added this method
}