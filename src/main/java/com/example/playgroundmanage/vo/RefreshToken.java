package com.example.playgroundmanage.vo;

import com.example.playgroundmanage.dto.TokenEdit;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String refreshToken;

    @Builder
    public RefreshToken(Long id, String username, String refreshToken) {
        this.id = id;
        this.username = username;
        this.refreshToken = refreshToken;
    }

    @Transactional
    public RefreshToken update(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
