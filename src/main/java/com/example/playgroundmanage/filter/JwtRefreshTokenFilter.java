package com.example.playgroundmanage.filter;

import com.example.playgroundmanage.exception.TokenNotValidException;
import com.example.playgroundmanage.login.auth.JwtTokenProvider;
import com.example.playgroundmanage.login.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.example.playgroundmanage.Constant.ACCESS_TOKEN_HEADER_NAME;
import static com.example.playgroundmanage.Constant.ACCESS_TOKEN_REFRESH_URL;
import static com.example.playgroundmanage.login.auth.JwtTokenProvider.ACCESS_TOKEN_EXPIRATION;

@RequiredArgsConstructor
public class JwtRefreshTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       if(skipFilter(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
       }
        String refreshToken = resolveRefreshToken(request);
        if(jwtTokenProvider.validateToken(refreshToken) && isRefreshTokenExist(refreshToken)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
            String accessToken = jwtTokenProvider.generateToken(authentication, ACCESS_TOKEN_EXPIRATION);
            response.setHeader(ACCESS_TOKEN_HEADER_NAME, accessToken);
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private boolean isRefreshTokenExist(String refreshToken) {
        return tokenRepository.existsRefreshTokenByRefreshToken(refreshToken);
    }

    private boolean skipFilter(String url) {
        List<String> list = List.of(
                ACCESS_TOKEN_REFRESH_URL);

        return list.stream()
                .filter(s -> s.contains(url))
                .toList().size() == 0;
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("RefreshToken");
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }
        throw new TokenNotValidException();
    }
}
