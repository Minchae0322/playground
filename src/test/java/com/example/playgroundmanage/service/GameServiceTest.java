package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.GameRegistration;
import com.example.playgroundmanage.dto.response.SubTeamDto;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.GameService;
import com.example.playgroundmanage.game.service.UserService;
import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.game.vo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameServiceTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CompetingTeamRepository competingTeamRepository;

    @Autowired
    private MatchParticipantRepository matchParticipantRepository;

    @Autowired
    private GameService gameService;
    private User testUser;

    private Team testTeam;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SubTeamRepository subTeamRepository;

    @BeforeEach
    void before() {
        gameRepository.deleteAll();
        competingTeamRepository.deleteAll();
        subTeamRepository.deleteAll();
        testUser = User.builder()
                .username("test")
                .nickname("test")
                .role(UserRole.USER)
                .isEnable(true)
                .build();
        userRepository.save(testUser);
        testTeam = Team.builder()
                .sportsEvent(SportsEvent.SOCCER)
                .leader(testUser)
                .teamName("testTeam")
                .build();
        teamRepository.save(testTeam);
    }

    public Long initGame() {
        GameRegistration gameRegistration = GameRegistration.builder()
                .matchStart(LocalDateTime.now(ZoneId.of(("Asia/Seoul"))))
                .runningTime(60L)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        return gameService.createGame(gameRegistration);
    }


    @Test
    void start_match() {
        GameRegistration gameRegistration = GameRegistration.builder()
                .matchStart(LocalDateTime.now(ZoneId.of(("Asia/Seoul"))))
                .runningTime(60L)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();

        Game game = gameRepository.findById(gameService.createGame(gameRegistration)).orElseThrow();


        Assertions.assertEquals(1, gameRepository.count());
        Assertions.assertEquals(1L, game.getHomeTeam().getId());
        Assertions.assertEquals(2L, game.getAwayTeam().getId());
        assertEquals("test", game.getHost().getUsername());
    }

    @Test
    void getMatchBeforeStarted() {
        GameRegistration gameRegistration = GameRegistration.builder()
                .matchStart(LocalDateTime.of(2081, 12, 2, 7, 10))
                .runningTime(60L)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        gameService.createGame(gameRegistration);


        Assertions.assertEquals(1, gameRepository.count());


        List<Game> games = userService.getHostCreatedGamesNotStarted(testUser);
        assertEquals(1, games.size());
    }


    @Test
    void testExcludePastGames() {
        GameRegistration gameRegistration = GameRegistration.builder()
                .matchStart(LocalDateTime.of(2023, 11, 2, 7, 10))
                .runningTime(60L)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        gameService.createGame(gameRegistration);


        Assertions.assertEquals(1, gameRepository.count());


        List<Game> games = userService.getHostCreatedGamesNotStarted(testUser);
        assertEquals(0, games.size());
    }

    @Test
    void create_solo_team() {
        Game game = gameRepository.findById(initGame()).orElseThrow();

        assertEquals(2, subTeamRepository.count());
        assertTrue(game.getHomeTeam().isContainSoloTeam());
        assertTrue(game.getAwayTeam().isContainSoloTeam());
        assertTrue(game.getHomeTeam().getSubTeams().get(0).isAccept());

    }


    @Test
    void start_many_match() {
        List<GameRegistration> gameRegistrationList = IntStream.range(0,19)
                .mapToObj(i -> GameRegistration.builder()
                        .matchStart(LocalDateTime.now())
                        .runningTime((long) i)
                        .host(testUser)
                        .sportsEvent(SportsEvent.SOCCER)
                        .build()).toList();
        List<Long> games = gameRegistrationList.stream()
                .map(m -> gameService.createGame(m))
                .toList();

        Assertions.assertEquals(19, gameRepository.count());
        Assertions.assertEquals(38, competingTeamRepository.count());
    }

    @Test
    void 홈팀에_참여하는_subTeam() {
        Long gameId = gameService.createGame(GameRegistration.builder()
                .matchStart(LocalDateTime.now().plusMinutes(1))
                .runningTime((long) 60)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build());

        SubTeam subTeam = SubTeam.builder()
                .team(testTeam)
                .isSoloTeam(false)
                .competingTeam(gameRepository.findById(gameId).get().getHomeTeam())
                .build();
        subTeamRepository.save(subTeam);

        List<SubTeamDto> subTeamDtos = gameService.getTeamsBySide(gameId, MatchTeamSide.HOME);
        assertEquals(1, subTeamDtos.size());
        assertEquals("testTeam",subTeamDtos.stream().findFirst().get().getTeamName());
    }

}