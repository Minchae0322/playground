package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.date.MyDateTimeLocal;
import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.dto.UsersGameDto;
import com.example.playgroundmanage.game.repository.GameParticipantRepository;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.UserRepository;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameServiceTestReal {


    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameParticipantRepository gameParticipantRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserRepository userRepository;

    private User testUser;


    @BeforeEach
    void before() {
        testUser = User.builder()
                .username("test")
                .nickname("test")
                .role(UserRole.USER)
                .isEnable(true)
                .build();
        userRepository.save(testUser);
    }
    public Long generateGame(GameDto gameDto) {
        return gameService.generateGame(2L, gameDto);
    }


    @Test
    void getUsersGameInMonthAsc() {
        GameDto gameDto = GameDto.builder()
                .gameName("test")
                .sportsEvent(SportsEvent.SOCCER)
                .runningTime(60)
                .host(testUser)
                .startDateTime(MyDateTimeLocal.initMyDateTime(LocalDateTime.of(2024, 2, 5, 1, 0)))
                .build();
        Long gameId1 = generateGame(gameDto);
        Game game1 = gameRepository.findById(gameId1).orElseThrow();
        gameParticipantRepository.save(GameParticipant.builder()
                        .user(testUser)
                        .isAccepted(true)
                        .game(game1)
                .build());

        System.out.println("ssss" + LocalDateTime.now());
        List<UsersGameDto.UsersGameResponseDto> responseDtoList = gameService.getMonthGameAsc(UsersGameDto.UsersGameRequestDto.builder()
                        .user(testUser)
                        .myDateTime(MyDateTimeLocal.initMyDateTime(LocalDateTime.of(2024, 2, 1, 0, 0)))
                .build());
        System.out.println("ssss" + LocalDateTime.now());
        responseDtoList.size();
    }
}