package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUsername(String username);
}
