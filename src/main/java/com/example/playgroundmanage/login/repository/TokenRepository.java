package com.example.playgroundmanage.login.repository;

import com.example.playgroundmanage.login.vo.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUsername(String username);

    boolean existsRefreshTokenByUsername(String username);
}
