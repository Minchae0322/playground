package com.example.playgroundmanage.auth;

import com.example.playgroundmanage.vo.RefreshToken;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        List<String> list = List.of(
                "/auth",
                "/home",
                "/board/posts");

        /*if (request.getMethod().equals("OPTIONS")) {
            response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");

            response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
            return;
        }*/

        String url = String.valueOf(request.getRequestURL());
        for (String s : list) {
            if (url.contains(s)) {
                filterChain.doFilter(request, response);
                return;
            }
        }


        String accessToken = resolveToken(request);

            // 2. validateToken 으로 토큰 유효성 검사
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;
            }

            RefreshToken refreshToken = jwtTokenProvider.getRefreshToken(accessToken);

            if(accessToken != null && jwtTokenProvider.validateToken(refreshToken.getRefreshToken())) {
                Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken.getRefreshToken());
                String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setHeader("Authorization", newAccessToken);
                response.setStatus(HttpServletResponse.SC_OK);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            //response.sendRedirect("/auth/login");



    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)) {
            return bearerToken;
        }
        throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
    }
}
