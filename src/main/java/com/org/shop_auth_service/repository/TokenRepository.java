package com.org.shop_auth_service.repository;

import com.org.shop_auth_service.model.token.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    @Transactional
    void deleteAllByUserId(Long userId);
}
