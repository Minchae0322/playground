package com.example.playgroundmanage.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

public class UsernamePasswordCustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public UsernamePasswordCustomAuthenticationFilter() {
        super("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        UsernamePassword usernamePassword = new ObjectMapper().readValue(request.getInputStream(), UsernamePassword.class);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken.unauthenticated(
                usernamePassword.username,
                usernamePassword.password
        );

        usernamePasswordAuthenticationToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }

    @Getter
    static class UsernamePassword {
        String username;
        String password;
    }
}
