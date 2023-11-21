package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.TokenEdit;
import com.example.playgroundmanage.repository.TokenRepository;
import com.example.playgroundmanage.vo.RefreshToken;
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
