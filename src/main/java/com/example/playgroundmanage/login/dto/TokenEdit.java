package com.example.playgroundmanage.login.dto;


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
}
