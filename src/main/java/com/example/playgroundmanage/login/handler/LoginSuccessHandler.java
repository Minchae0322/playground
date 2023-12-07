package com.example.playgroundmanage.login.handler;

import com.example.playgroundmanage.login.auth.JwtTokenProvider;
import com.example.playgroundmanage.login.auth.TokenInfo;
import com.example.playgroundmanage.login.dto.TokenEdit;
import com.example.playgroundmanage.login.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

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

        response.setStatus(HttpServletResponse.SC_OK);
        redirect(request, response,tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response,
                         String accessToken, String refreshToken) throws IOException {

        String uri = createURI(accessToken, refreshToken).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }



    private URI createURI(String accessToken, String refreshToken) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);

        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port(5173)
                .path("/oauth2/redirect")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
