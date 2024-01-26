package com.example.playgroundmanage.login.service;

import com.example.playgroundmanage.login.dto.TokenEdit;
import com.example.playgroundmanage.login.repository.TokenRepository;
import com.example.playgroundmanage.login.vo.RefreshToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    @Transactional
    public RefreshToken updateRefreshToken(TokenEdit tokenEdit) {
        return tokenRepository.save(tokenRepository.findByUsername(tokenEdit.getUsername())
                .orElse(tokenEdit.toEntity())
                .update(tokenEdit.getRefreshToken()));
    }

    public RefreshToken getRefreshToken(String username) {
        return tokenRepository.findByUsername(username).orElseThrow();
    }

    public boolean isExistRefreshToken(String username) {
        return tokenRepository.existsRefreshTokenByUsername(username);
    }

    public void save(String username, String refreshToken) {
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .username(username)
                .refreshToken(refreshToken)
                .build();
        tokenRepository.save(refreshTokenEntity);
    }


}
