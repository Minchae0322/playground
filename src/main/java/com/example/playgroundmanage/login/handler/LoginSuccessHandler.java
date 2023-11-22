package com.example.playgroundmanage.login.handler;

import com.example.playgroundmanage.login.auth.JwtTokenProvider;
import com.example.playgroundmanage.login.auth.TokenInfo;
import com.example.playgroundmanage.login.dto.TokenEdit;
import com.example.playgroundmanage.login.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;


@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        TokenInfo tokenInfo = jwtTokenProvider.generateAccessAndRefreshTokens(authentication);

        TokenEdit tokenEdit = TokenEdit.builder()
                .username(authentication.getName())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();

        tokenService.updateRefreshToken(tokenEdit);

        response.setHeader("Authorization", tokenInfo.getAccessToken());
        response.setHeader("RefreshToken", tokenInfo.getRefreshToken());
        response.setStatus(HttpServletResponse.SC_OK);
        //response.sendRedirect("/loginInfo");
    }
}
