package com.example.playgroundmanage.filter;

import com.example.playgroundmanage.login.auth.JwtTokenProvider;
import com.example.playgroundmanage.exception.TokenNotValidException;
import com.example.playgroundmanage.login.vo.RefreshToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.example.playgroundmanage.login.auth.JwtTokenProvider.ACCESS_TOKEN_EXPIRATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(skipFilter(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        //토큰이 존재하지 않으면 403 Exception
        String accessToken = resolveAccessToken(request);

        if (jwtTokenProvider.validateToken(accessToken)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            return;
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setHeader("OriginalUrl", request.getRequestURI());
        response.sendRedirect("/token/refresh");
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }

        throw new TokenNotValidException();
    }

    private boolean skipFilter(String url) {
        List<String> list = List.of(
                "/auth"
                , "/token/refresh"
                , "/login");

        return list.stream()
                .filter(s -> s.contains(url))
                .toList().size() != 0;

    }
}
