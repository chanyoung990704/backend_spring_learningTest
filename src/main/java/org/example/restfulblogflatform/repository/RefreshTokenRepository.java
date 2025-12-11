package org.example.restfulblogflatform.repository;

import org.example.restfulblogflatform.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    boolean existsByUserIdAndRevokedFalseAndExpiryDateAfter(Long userId, LocalDateTime now);

    List<RefreshToken> findAllByUserId(Long userId);
}
