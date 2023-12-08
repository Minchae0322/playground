package com.example.playgroundmanage.service;

import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.UserRepository;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.repository.PlaygroundRepository;
import com.example.playgroundmanage.vo.Playground;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlaygroundServiceTest {

    @Autowired
    private PlaygroundRepository playgroundRepository;

    @Autowired
    private PlaygroundService playgroundService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    private Playground testPlayground;

    @BeforeEach
    void before() {
        testPlayground = Playground.builder()
                .name("test")
                .build();
        playgroundRepository.save(testPlayground);
    }


    @Test
    void 현재_진행중인_경기_불러오기() {
        //given 운동장안에 9개의 경기가 있다.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime afterOneMinute = LocalDateTime.now().plusMinutes(1);

        List<Game> gameList = IntStream.range(0, 10)
                .mapToObj(i -> Game.builder()
                        .gameStartDateTime(now.plusMinutes(60L))
                        .runningTime(59L)
                        .playground(testPlayground)
                        .build()).toList();
        gameRepository.saveAll(gameList);

        assertEquals(10, gameRepository.count());

        //when

        Game game = playgroundService.getGameInProgress(testPlayground.getId(), afterOneMinute);

        //then
        assertEquals(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), game.getGameStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }

    @Test
    void 현재_진행중인_경기_불러오기_60분_후() {
        //given 운동장안에 9개의 경기가 있다.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime after = LocalDateTime.now().plusMinutes(60);

        List<Game> gameList = IntStream.range(0, 10)
                .mapToObj(i -> Game.builder()
                        .gameStartDateTime(now.plusMinutes(60L))
                        .runningTime(59L)
                        .playground(testPlayground)
                        .build()).toList();
        gameRepository.saveAll(gameList);

        assertEquals(10, gameRepository.count());

        //when

        Game game = playgroundService.getGameInProgress(testPlayground.getId(), after);

        //then
        assertEquals(now.plusMinutes(60).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), game.getGameStartDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}