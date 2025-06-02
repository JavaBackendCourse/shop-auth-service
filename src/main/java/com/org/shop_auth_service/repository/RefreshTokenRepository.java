package com.org.shop_auth_service.repository;

import com.org.shop_auth_service.model.token.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String refreshToken);

    @Transactional
    void deleteAllByUserId(Long userId);
}
