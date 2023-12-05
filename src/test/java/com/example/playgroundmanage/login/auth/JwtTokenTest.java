package com.example.playgroundmanage.login.auth;

import com.example.playgroundmanage.login.dto.TokenEdit;
import com.example.playgroundmanage.game.repository.UserRepository;
import com.example.playgroundmanage.login.service.TokenService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.type.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtTokenTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  TokenService tokenService;

    private final static String REQUEST_URL = "/Info";

    private final static String REFRESH_TOKEN_URL = "/token/refresh";
    @Autowired
    private MockMvc mockMvc;

    private Authentication oAuth2User;

    @BeforeEach
    void before() {
        userRepository.deleteAll();
        User testUser = User.builder()
                .username("testUser")
                .provider("own")
                .role(UserRole.USER)
                .build();
        userRepository.save(testUser);
        MyUserDetails userDetails = new MyUserDetails(testUser);
        oAuth2User = new OAuth2AuthenticationToken(userDetails, userDetails.getAuthorities(), "naver");
    }

    @Test
    void accessToken_valid() throws Exception {
        String accessToken = jwtTokenProvider.generateToken(oAuth2User, 10 * 1000L);
        mockMvc.perform(MockMvcRequestBuilders.get(REQUEST_URL).header("Authorization", accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("wrong AccessToken, redirect to refreshToken Url")
    void accessToken_not_valid() throws Exception {
        String dummy = "dummy";
        mockMvc.perform(MockMvcRequestBuilders.get(REQUEST_URL).header("Authorization", dummy))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andDo(print());
    }

    @Test
    void accessToken_not_valid_refreshToken_valid() throws Exception {
        TokenInfo tokenInfo = jwtTokenProvider.generateAccessAndRefreshTokens(oAuth2User);

        TokenEdit tokenEdit = TokenEdit.builder()
                .username(oAuth2User.getName())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();

        tokenService.updateRefreshToken(tokenEdit);

        accessToken_not_valid();

        mockMvc.perform(MockMvcRequestBuilders.get(REFRESH_TOKEN_URL).header("RefreshToken", tokenInfo.getRefreshToken())
                        .header("originalUrl", "/Info"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    void accessToken_notValid_refreshToken_notValid() throws Exception {
        String dummy = "dummy";
        accessToken_not_valid();

        mockMvc.perform(MockMvcRequestBuilders.get(REFRESH_TOKEN_URL).header("RefreshToken", dummy)
                        .header("originalUrl", "/Info"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andDo(print());
    }
}
