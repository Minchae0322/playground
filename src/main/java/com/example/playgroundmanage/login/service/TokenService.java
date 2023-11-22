package com.example.playgroundmanage.login.service;

import com.example.playgroundmanage.login.dto.TokenEdit;
import com.example.playgroundmanage.login.repository.TokenRepository;
import com.example.playgroundmanage.login.vo.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public RefreshToken updateRefreshToken(TokenEdit tokenEdit) {
        RefreshToken refreshToken = getRefreshToken(tokenEdit.getUsername());
        return refreshToken.update(tokenEdit.getRefreshToken());
    }

    public RefreshToken getRefreshToken(String username) {
        return tokenRepository.findByUsername(username).orElseThrow();
    }

}
