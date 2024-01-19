package com.example.playgroundmanage.service;

import com.example.playgroundmanage.login.dto.OAuth2UserProfile;
import com.example.playgroundmanage.login.service.UserDetailsServiceImpl;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.location.respository.TeamRepository;
import com.example.playgroundmanage.game.repository.UserRepository;
import com.example.playgroundmanage.type.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.BDDAssumptions.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@SpringBootTest
class OAuth2ServiceTest {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserDetailsServiceImpl oAuth2Service;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TeamRepository teamRepository;


    @BeforeEach
    void before() {
        gameRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test

    void signupWithOAuth2_github() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("login", "asdf1234");
        OAuth2User oAuth2GithubUser = new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(UserRole.USER.getValue())), attributes, "login");

        OAuth2UserProfile oAuth2UserProfile = oAuth2Service.getOAuth2UserProfile("github", oAuth2GithubUser);
        assertEquals("asdf1234", oAuth2UserProfile.getUsername());
        assertEquals("github", oAuth2UserProfile.getProvider());
    }
}