package com.example.playgroundmanage.service;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.GameService;
import com.example.playgroundmanage.game.service.UserService;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.respository.TeamRepository;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.location.vo.Playground;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameServiceTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlaygroundRepository playgroundRepository;

    @Autowired
    private CompetingTeamRepository competingTeamRepository;

    @Autowired
    private GameParticipantRepository gameParticipantRepository;

    @Autowired
    private GameService gameService;
    private User testUser;

    private Team testTeam;

    private Playground testPlayground;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SubTeamRepository subTeamRepository;


    @BeforeEach
    @Transactional
    void before() {
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
        testPlayground = Playground.builder()
                .name("test")
                .build();
        playgroundRepository.save(testPlayground);
    }

/*
   public Long initGame() {
        GameDto gameRegistration = GameDto.builder()
                .myDateTime(MyDateTime.getMyDateTime(ZonedDateTime.now()))
                .runningTime(60)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        return gameService.generateGame(gameRegistration);
    }
*/

    @Test
    @Transactional
    void 게임_생성() {
        GameDto gameRegistration = GameDto.builder()
                .startDateTime(MyDateTime.initMyDateTime(ZonedDateTime.now().plusMinutes(10)))
                .runningTime(60)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();

        Long gameId = gameService.generateGame(testPlayground.getId(), gameRegistration);


        assertNotNull(gameId);
    }
 /*

    @Test
    void getMatchBeforeStarted() {
        GameDto gameRegistration = GameDto.builder()
                .matchStart(LocalDateTime.of(2081, 12, 2, 7, 10))
                .runningTime(60L)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        gameService.generateGame(gameRegistration);


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
        gameService.generateGame(gameRegistration);


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
                .map(m -> gameService.generateGame(m))
                .toList();

        Assertions.assertEquals(19, gameRepository.count());
        Assertions.assertEquals(38, competingTeamRepository.count());
    }

    @Test
    void 홈팀에_참여하는_subTeam() {
        Long gameId = gameService.generateGame(GameRegistration.builder()
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
    }*/

}