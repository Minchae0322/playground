package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.MatchRegistration;
import com.example.playgroundmanage.dto.SubTeamRegistrationParams;
import com.example.playgroundmanage.dto.TeamRegistration;
import com.example.playgroundmanage.dto.UserJoinTeamParams;
import com.example.playgroundmanage.repository.*;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.vo.Game;
import com.example.playgroundmanage.vo.SubTeam;
import com.example.playgroundmanage.vo.Team;
import com.example.playgroundmanage.vo.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MatchServiceTest {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private CompetingTeamRepository competingTeamRepository;

    @Autowired
    private  MatchParticipantRepository matchParticipantRepository;

    @Autowired
    private GameService gameService;
    private User testUser;

    private Team testTeam;

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
        MatchRegistration matchRegistration = MatchRegistration.builder()
                .matchStart(LocalDate.now())
                .runningTime(60L)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        return gameService.startMatch(matchRegistration);
    }


    @Test
    void start_match() {
        MatchRegistration matchRegistration = MatchRegistration.builder()
                .matchStart(LocalDate.now())
                .runningTime(60L)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        Game game = gameRepository.findById(initGame()).orElseThrow();


        Assertions.assertEquals(1, gameRepository.count());
        Assertions.assertEquals(2L, game.getHomeTeam().getId());
        Assertions.assertEquals(1L, game.getAwayTeam().getId());
        assertEquals("test", game.getHost().getUsername());
    }

    @Test
    void create_solo_team() {
        Game game = gameRepository.findById(initGame()).orElseThrow();
        gameService.generateSoloSubTeam(game.getHomeTeam());
        gameService.generateSoloSubTeam(game.getAwayTeam());

        Game game2 = gameRepository.findById(game.getId()).orElseThrow();

        assertEquals(2, subTeamRepository.count());
        assertTrue(game2.getHomeTeam().isContainSoloTeam());
        assertTrue(game2.getAwayTeam().isContainSoloTeam());

    }


    @Test
    void start_many_match() {
        List<MatchRegistration> matchRegistrationList = IntStream.range(0,19)
                .mapToObj(i -> MatchRegistration.builder()
                        .matchStart(LocalDate.now())
                        .runningTime((long) i)
                        .host(testUser)
                        .sportsEvent(SportsEvent.SOCCER)
                        .build()).toList();

        List<Long> games = matchRegistrationList.stream()
                .map(m -> gameService.startMatch(m))
                .toList();

        Assertions.assertEquals(19, gameRepository.count());
        Assertions.assertEquals(38, competingTeamRepository.count());
    }

    @Test
    void createSubTeam() {
        Long gameId = initGame();
        Game game = gameRepository.findById(gameId).orElseThrow();

        SubTeamRegistrationParams subTeamRegistrationParams = SubTeamRegistrationParams.builder()
                .user(testUser)
                .matchTeamId(game.getHomeTeam().getId())
                .teamId(testTeam.getId())
                .build();
        gameService.generateSubTeam(subTeamRegistrationParams);
        Game game2 = gameRepository.findById(gameId).orElseThrow();

        assertEquals(1, game2.getHomeTeam().getSubTeams().size());
        assertEquals("testTeam", game2.getHomeTeam().getSubTeams().get(0).getTeam().getTeamName());
    }

    @Test
    void joinSubTeam() {
        Long gameId = initGame();
        Game game = gameRepository.findById(gameId).orElseThrow();

        SubTeamRegistrationParams subTeamRegistrationParams = SubTeamRegistrationParams.builder()
                .user(testUser)
                .matchTeamId(game.getHomeTeam().getId())
                .teamId(testTeam.getId())
                .build();
        Long subTeamId = gameService.generateSubTeam(subTeamRegistrationParams);


        UserJoinTeamParams userJoinTeamParams = UserJoinTeamParams.builder()
                .subTeamId(subTeamId)
                .user(testUser)
                .build();
        gameService.joinUserInSubTeam(userJoinTeamParams);
        Game game2 = gameRepository.findById(gameId).orElseThrow();

        assertEquals(1, game2.getHomeTeam().getSubTeams().size());
        assertEquals("testTeam", game2.getHomeTeam().getSubTeams().get(0).getTeam().getTeamName());
        assertEquals(1, matchParticipantRepository.count());
        assertEquals("test", game2.getHomeTeam().getSubTeams().get(0).getMatchParticipants().get(0).getUser().getUsername());
    }




}