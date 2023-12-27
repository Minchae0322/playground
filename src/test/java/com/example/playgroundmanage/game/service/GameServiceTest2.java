package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.repository.PlaygroundRepository;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.vo.Playground;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GameServiceTest2 {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private PlaygroundRepository playgroundRepository;

    @InjectMocks
    private GameService gameService;

    private GameDto gameDto;
    private Playground playground;
    private User testUser;

    @BeforeEach
    void setUp() {
        // 초기 테스트 데이터 설정
        testUser =  User.builder()
                .username("test")
                .nickname("test")
                .role(UserRole.USER)
                .isEnable(true)
                .build();// 적절히 User 객체를 설정하세요
        playground = Playground.builder()
                .name("test")
                .build(); // 적절히 Playground 객체를 설정하세요


        // GameDto 객체 설정
        gameDto = GameDto.builder()
                .myDateTime(MyDateTime.getMyDateTime(ZonedDateTime.now().plusMinutes(2)))
                .runningTime(60)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        // Mock 설정
        when(playgroundRepository.findById(any())).thenReturn(Optional.of(playground));
        when(gameRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void testGenerateGame() {
        // 메소드 실행
        Long gameId = gameService.generateGame(playground.getId(), gameDto);

        // 결과 검증
        assertNotNull(gameId);
        // 추가 검증 로직이 필요하면 작성하세요
    }
}