package com.example.playgroundmanage.config;


import com.example.playgroundmanage.filter.JwtAuthenticationFilter;
import com.example.playgroundmanage.filter.JwtRefreshTokenFilter;
import com.example.playgroundmanage.login.auth.JwtTokenProvider;
import com.example.playgroundmanage.filter.UsernamePasswordCustomAuthenticationFilter;
import com.example.playgroundmanage.login.handler.LoginFailureHandler;
import com.example.playgroundmanage.login.handler.LoginSuccessHandler;
import com.example.playgroundmanage.game.repository.UserRepository;
import com.example.playgroundmanage.login.repository.TokenRepository;
import com.example.playgroundmanage.login.service.TokenService;
import com.example.playgroundmanage.login.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.RequestMatcherProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static com.example.playgroundmanage.Instance.ACCESS_TOKEN_HEADER_NAME;
import static com.example.playgroundmanage.Instance.SERVER_URL;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    private final TokenService tokenService;

    private final TokenRepository tokenRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtRefreshTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), JwtRefreshTokenFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .successHandler(new LoginSuccessHandler(jwtTokenProvider, tokenService, userRepository))
                        .failureHandler(new LoginFailureHandler())
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(userDetailsService))
                )
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

    @Bean
    public JwtRefreshTokenFilter jwtRefreshTokenFilter() {
        return new JwtRefreshTokenFilter(jwtTokenProvider, tokenRepository);
    }

    @Bean
    public UsernamePasswordCustomAuthenticationFilter usernamePasswordCustomAuthenticationFilter() {
        UsernamePasswordCustomAuthenticationFilter filter = new UsernamePasswordCustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(jwtTokenProvider, tokenService, userRepository));
        filter.setAuthenticationFailureHandler(new LoginFailureHandler());
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        filter.setAuthenticationManager(authenticationManage());
        return filter;
    }



    @Bean
    public AuthenticationManager authenticationManage() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(new UserDetailsServiceImpl(userRepository, tokenRepository));
        //provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }




    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://localhost:5173");
        configuration.addAllowedOrigin("https://" + SERVER_URL);
        configuration.addAllowedOrigin("https://localhost:8080");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader(ACCESS_TOKEN_HEADER_NAME);
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
