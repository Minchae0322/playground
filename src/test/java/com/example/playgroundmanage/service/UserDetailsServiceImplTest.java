package com.example.playgroundmanage.service;

import com.example.playgroundmanage.game.service.UserService;
import com.example.playgroundmanage.login.dto.UserSignupForm;
import com.example.playgroundmanage.exception.FormatException;
import com.example.playgroundmanage.login.service.UserDetailsServiceImpl;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.location.respository.TeamRepository;
import com.example.playgroundmanage.game.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDetailsServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TeamRepository teamRepository;


    @BeforeEach
    void init() {
        gameRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void signup_user() {
        UserSignupForm userSignupForm = UserSignupForm.builder()
                .username("abcde")
                .password("asdf1234")
                .build();
        userService.signup(userSignupForm);

        assertEquals(1, userRepository.count());
        assertTrue(userRepository.existsByUsername("abcde"));
    }

    @Test
    void signup_username_wrong_format_special_characters() {
        UserSignupForm userSignupForm = UserSignupForm.builder()
                .username("abc%%de")
                .password("!asdf1234")
                .build();
        assertThrows(FormatException.class, () ->  userService.signup(userSignupForm));
    }

    @Test
    void signup_username_wrong_format_first_number() {
        UserSignupForm userSignupForm = UserSignupForm.builder()
                .username("12abcde")
                .password("asdf1234")
                .build();
        assertThrows(FormatException.class, () ->  userService.signup(userSignupForm));
    }

    @Test
    void signup_password_wrong_format_letters_min() {
        UserSignupForm userSignupForm = UserSignupForm.builder()
                .username("abcde")
                .password("asdf14")
                .build();
        assertThrows(FormatException.class, () ->  userService.signup(userSignupForm));
    }

    @Test
    void signup_password_wrong_format_letters_max() {
        UserSignupForm userSignupForm = UserSignupForm.builder()
                .username("abcde")
                .password("asdf14aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
                .build();
        assertThrows(FormatException.class, () ->  userService.signup(userSignupForm));
    }

    @Test
    void loadByusername() {
        UserSignupForm userSignupForm = UserSignupForm.builder()
                .username("abcde")
                .password("asdf1234")
                .build();
        userService.signup(userSignupForm);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userSignupForm.getUsername());
        assertEquals("abcde", userDetails.getUsername());
        assertThat(passwordEncoder.matches("asdf1234",userDetails.getPassword())).isTrue();
    }


}