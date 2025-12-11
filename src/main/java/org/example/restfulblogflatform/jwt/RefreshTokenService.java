package org.example.restfulblogflatform.jwt;

import lombok.RequiredArgsConstructor;
import org.example.restfulblogflatform.entity.RefreshToken;
import org.example.restfulblogflatform.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken storeRefreshToken(Long userId, String token, LocalDateTime expiryDate) {
        RefreshToken refreshToken = RefreshToken.create(userId, token, expiryDate);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken rotateRefreshToken(String oldToken, Long userId, String newToken, LocalDateTime expiryDate) {
        revokeToken(oldToken);
        return storeRefreshToken(userId, newToken, expiryDate);
    }

    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshToken -> {
                    refreshToken.revoke();
                    refreshTokenRepository.save(refreshToken);
                });
    }

    public void revokeAllForUser(Long userId) {
        refreshTokenRepository.findAllByUserId(userId)
                .forEach(refreshToken -> {
                    refreshToken.revoke();
                    refreshTokenRepository.save(refreshToken);
                });
    }

    @Transactional(readOnly = true)
    public Optional<RefreshToken> findValidToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .filter(refreshToken -> !refreshToken.isRevoked() && refreshToken.getExpiryDate().isAfter(LocalDateTime.now()));
    }

    @Transactional(readOnly = true)
    public boolean hasActiveToken(Long userId) {
        return refreshTokenRepository.existsByUserIdAndRevokedFalseAndExpiryDateAfter(userId, LocalDateTime.now());
    }
}
