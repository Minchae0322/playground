package com.example.playgroundmanage.althlectis.service;

import com.example.playgroundmanage.althlectis.service.impl.FriendlyAthleticsGenerator;
import com.example.playgroundmanage.date.MyDateTimeLocal;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.vo.Campus;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.althlectis.dto.GameGenerationRequest;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class FriendlyAthleticsGeneratorTest {

    @MockBean
    private PlaygroundRepository playgroundRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private FriendlyAthleticsGenerator friendlyAthleticsGenerator;

    @Test
    void generate() {
        //given
        GameGenerationRequest gameGenerationRequest = new GameGenerationRequest(
                1L,
                GameType.FRIENDLY,
                "test Game",
                MyDateTimeLocal.initMyDateTime(LocalDateTime.of(9999, 1, 1, 1, 1)),
                SportsEvent.SOCCER,
                60);

        Playground playground = Playground.builder()
                .campus(Campus.builder().build())
                .name("play1")
                .sportsEvent(SportsEvent.SOCCER)
                .games(new ArrayList<>())
                .build();

        User host = User.builder()
                .id(1L)
                .role(UserRole.USER)
                .isLoggedIn(true)
                .isEnable(true)
                .username("host")
                .provider("naver")
                .nickname("host")
                .build();

        //when
        when(playgroundRepository.findById(1L)).thenReturn(Optional.of(playground));
        when(userRepository.findById(any())).thenReturn(Optional.of(host));



    }
}