package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.MatchRegistration;
import com.example.playgroundmanage.dto.SubTeamRegistrationParams;
import com.example.playgroundmanage.dto.TeamRegistration;
import com.example.playgroundmanage.repository.*;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.vo.Game;
import com.example.playgroundmanage.vo.SubTeam;
import com.example.playgroundmanage.vo.Team;
import com.example.playgroundmanage.vo.User;
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
    private MatchRepository matchRepository;

    @Autowired
    private MatchTeamRepository matchTeamRepository;

    @Autowired
    private  MatchParticipantRepository matchParticipantRepository;

    @Autowired
    private GameService gameService;
    private User testUser;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private SubTeamRepository subTeamRepository;

    @BeforeEach
    void before() {
        matchRepository.deleteAll();
        matchTeamRepository.deleteAll();
        subTeamRepository.deleteAll();
        testUser = User.builder()
                .username("test")
                .role(UserRole.USER)
                .isEnable(true)
                .build();
        userRepository.save(testUser);
    }

    public Game initMatch() {
        MatchRegistration matchRegistration = MatchRegistration.builder()
                .matchStart(LocalDate.now())
                .runningTime(60L)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        return gameService.startMatch(matchRegistration);
    }

    public SubTeam initSmallTeam() {

        Game game = initMatch();

        Long teamId = teamService.generateTeam(TeamRegistration.builder()
                .leader(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .teamName("team")
                .build());
        Team team = teamService.findByTeamId(teamId);

        SubTeamRegistrationParams subTeamRegistrationParams = SubTeamRegistrationParams.builder()
                .teamId(team.getId())
                .matchTeamSide(MatchTeamSide.HOME)
                .matchTeamId(game.getHomeTeam().getId())
                .user(testUser)
                .build();

        Long smallTeamId = gameService.generateSubTeam(subTeamRegistrationParams);
        return subTeamRepository.findById(smallTeamId).orElseThrow();
    }

    @Test
    void start_match() {
        MatchRegistration matchRegistration = MatchRegistration.builder()
                .matchStart(LocalDate.now())
                .runningTime(60L)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        Game game = gameService.startMatch(matchRegistration);
        Assertions.assertEquals(1, matchRepository.count());
        Assertions.assertEquals(2L, game.getHomeTeam().getId());
        Assertions.assertEquals(1L, game.getAwayTeam().getId());
        assertEquals("test", game.getHost().getUsername());
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

        List<Game> games = matchRegistrationList.stream()
                .map(m -> gameService.startMatch(m))
                .toList();

        Assertions.assertEquals(19, matchRepository.count());
        Assertions.assertEquals(18L, games.get(18).getRunningTime());
        Assertions.assertEquals(38L, games.get(18).getHomeTeam().getId());
        Assertions.assertEquals(37L, games.get(18).getAwayTeam().getId());
        Assertions.assertEquals(38, matchTeamRepository.count());
    }

    @Test
    void generate_small_team() {
        long startTime = System.currentTimeMillis();
        SubTeam subTeam = initSmallTeam();
        long stopTime = System.currentTimeMillis();
        assertEquals(1, matchParticipantRepository.count());
        System.out.println(stopTime - startTime);
    }



}