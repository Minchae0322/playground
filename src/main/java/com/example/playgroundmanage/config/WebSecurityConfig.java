package com.example.playgroundmanage.config;


import com.example.playgroundmanage.filter.JwtAuthenticationFilter;
import com.example.playgroundmanage.filter.JwtRefreshTokenFilter;
import com.example.playgroundmanage.login.auth.JwtTokenProvider;
import com.example.playgroundmanage.filter.UsernamePasswordCustomAuthenticationFilter;
import com.example.playgroundmanage.login.handler.LoginFailureHandler;
import com.example.playgroundmanage.login.handler.LoginSuccessHandler;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.service.TokenService;
import com.example.playgroundmanage.login.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                //.addFilterBefore(jwtAuthenticationFilter(), OAuth2LoginAuthenticationFilter.class)
                .addFilterBefore(jwtRefreshTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), JwtRefreshTokenFilter.class)
               // .addFilterBefore(jwtRefreshTokenFilter(), OAuth2LoginAuthenticationFilter.class)
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.loginPage("/login"))
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/home"))
                .csrf(AbstractHttpConfigurer::disable)
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .defaultSuccessUrl("/loginInfo")
                        .successHandler(new LoginSuccessHandler(jwtTokenProvider, tokenService))
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(userDetailsService))
                )
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public JwtRefreshTokenFilter jwtRefreshTokenFilter() {
        return new JwtRefreshTokenFilter(jwtTokenProvider);
    }

    @Bean
    public UsernamePasswordCustomAuthenticationFilter usernamePasswordCustomAuthenticationFilter() {
        UsernamePasswordCustomAuthenticationFilter filter = new UsernamePasswordCustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(jwtTokenProvider, tokenService));
        filter.setAuthenticationFailureHandler(new LoginFailureHandler());
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        filter.setAuthenticationManager(authenticationManage());
        return filter;
    }



    @Bean
    public AuthenticationManager authenticationManage() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(new UserDetailsServiceImpl(userRepository));
        //provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }




    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("http://nid.naver.com");
        configuration.addAllowedOrigin("http://13.125.165.102");
        configuration.addAllowedOrigin("https://www.youtube.com");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("Authorization");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }




}
