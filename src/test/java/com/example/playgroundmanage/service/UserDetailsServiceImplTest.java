package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.UserSignupForm;
import com.example.playgroundmanage.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDetailsServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void init() {
        userRepository.deleteAll();
    }

    @Test
    void signup_user() {
        UserSignupForm userSignupForm = UserSignupForm.builder()
                .username("abcde")
                .password("asdf1234")
                .build();
        userDetailsService.signup(userSignupForm);

        assertEquals(1, userRepository.count());
        assertTrue(userRepository.existsByUsername("abcde"));
    }


}