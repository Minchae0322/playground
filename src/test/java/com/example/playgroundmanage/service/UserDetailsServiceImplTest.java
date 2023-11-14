package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.UserSignupForm;
import com.example.playgroundmanage.exception.FormatException;
import com.example.playgroundmanage.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

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

    @Test
    void signup_username_wrong_format_special_characters() {
        UserSignupForm userSignupForm = UserSignupForm.builder()
                .username("abc%%de")
                .password("!asdf1234")
                .build();
        assertThrows(FormatException.class, () -> userDetailsService.signup(userSignupForm));
    }

    @Test
    void signup_username_wrong_format_first_number() {
        UserSignupForm userSignupForm = UserSignupForm.builder()
                .username("12abcde")
                .password("asdf1234")
                .build();
        assertThrows(FormatException.class, () -> userDetailsService.signup(userSignupForm));
    }

    @Test
    void signup_password_wrong_format_letters_min() {
        UserSignupForm userSignupForm = UserSignupForm.builder()
                .username("abcde")
                .password("asdf14")
                .build();
        assertThrows(FormatException.class, () -> userDetailsService.signup(userSignupForm));
    }

    @Test
    void signup_password_wrong_format_letters_max() {
        UserSignupForm userSignupForm = UserSignupForm.builder()
                .username("abcde")
                .password("asdf14aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                .build();
        assertThrows(FormatException.class, () -> userDetailsService.signup(userSignupForm));
    }



}