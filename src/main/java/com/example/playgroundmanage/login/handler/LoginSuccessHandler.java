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
        request.getRequestURI();
        response.setHeader("Authorization", tokenInfo.getAccessToken());
        response.setHeader("RefreshToken", tokenInfo.getRefreshToken());
        //response.sendRedirect("/tokenInfo");
        response.setStatus(HttpServletResponse.SC_OK);
        //response.sendRedirect("/loginInfo");
        redirect(request, response,"dd", new ArrayList<>());
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response,
                          String username, List<String> authorities) throws IOException {
        String accessToken = "delegateAccessToken(username, authorities);";
        String refreshToken = "delegateRefreshToken(username);";

        String uri = "http://localhost:5173/login";
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
                .port(80)
                .path("/receive-token.html")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
