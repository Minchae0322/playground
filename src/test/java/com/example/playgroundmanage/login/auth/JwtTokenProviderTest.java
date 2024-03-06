package com.example.playgroundmanage.login.auth;

import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;



@SpringBootTest
class JwtTokenProviderTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    private Authentication oAuth2User;

    @BeforeEach
    void before() {
        userRepository.deleteAll();
        User user = User.builder()
                .username("testUser")
                .provider("own")
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
        MyUserDetails userDetails = new MyUserDetails(user);
        oAuth2User = new OAuth2AuthenticationToken(userDetails, userDetails.getAuthorities(), "naver");
    }

    @Test
    void generate_token() {
        String accessToken = jwtTokenProvider.generateToken(oAuth2User, JwtTokenProvider.ACCESS_TOKEN_EXPIRATION);
        Assertions.assertNotNull(accessToken);
    }

    @Test
    void decode_token() {
        String accessToken = jwtTokenProvider.generateToken(oAuth2User, JwtTokenProvider.ACCESS_TOKEN_EXPIRATION);
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        Assertions.assertNotNull(authentication);
    }

    @Test
    void decode_token_userDetail() {
        String accessToken = jwtTokenProvider.generateToken(oAuth2User, JwtTokenProvider.ACCESS_TOKEN_EXPIRATION);
        MyUserDetails authentication = (MyUserDetails) jwtTokenProvider.getAuthentication(accessToken).getPrincipal();
        Assertions.assertEquals("testUser", authentication.getName());
    }

    @Test
    void token_value_valid() {
        String accessToken = jwtTokenProvider.generateToken(oAuth2User, JwtTokenProvider.ACCESS_TOKEN_EXPIRATION);
        String dummyDate = "dkqwkejkqjek";
        Assertions.assertFalse(jwtTokenProvider.validateToken(accessToken + dummyDate));
    }
}