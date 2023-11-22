package com.example.playgroundmanage.login.dto;


import com.example.playgroundmanage.login.vo.RefreshToken;
import com.fasterxml.jackson.databind.deser.BuilderBasedDeserializer;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenEdit {

    private String username;

    private String refreshToken;

    @Builder
    public TokenEdit(String username, String refreshToken) {
        this.username = username;
        this.refreshToken = refreshToken;
    }

    public RefreshToken toEntity() {
        return RefreshToken.builder()
                .username(username)
                .refreshToken(refreshToken)
                .build();
    }
}
