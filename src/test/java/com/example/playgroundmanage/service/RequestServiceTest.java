package com.example.playgroundmanage.service;

import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.GameService;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.request.service.impl.SoloGameJoinRequestService;

import com.example.playgroundmanage.location.respository.TeamRepository;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.login.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RequestServiceTest {
    @Autowired
    public SoloGameJoinRequestService soloJoinGameRequestService;

    @Autowired
    private GameParticipantRepository gameParticipantRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRequestRepository gameRequestRepository;



    private User testUser;

    private Team testTeam;

    private Team testTeam2;

    @Autowired
    private UserRepository userRepository;



    @BeforeEach
    void before() {
        gameRequestRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();
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
        testTeam2 = Team.builder()
                .sportsEvent(SportsEvent.SOCCER)
                .leader(testUser)
                .teamName("testTeam2")
                .build();
        teamRepository.save(testTeam2);

    }

    public Long initGame() {
       /* GameRegistration gameRegistration = GameRegistration.builder()
                .matchStart(LocalDateTime.now(ZoneId.of(("Asia/Seoul"))))
                .runningTime(60L)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();*/
        return null; //gameService.generateGame(gameRegistration);
    }

   /* @Test
    void 팀으로_게임에_참여하는_요청을_보냄() {
        Game game = gameRepository.findById(initGame()).orElseThrow();
        SubTeamRegistrationParams subTeamRegistrationParams = SubTeamRegistrationParams.builder()
                .teamId(testTeam.getId())
                .user(testUser)
                .matchTeamSide(MatchTeamSide.HOME)
                .gameId(game.getId())
                .build();

        Long requestId = soloJoinGameRequestService.createSubTeamRequest(subTeamRegistrationParams);
        JoinGameRequest joinGameRequest = joinGameRequestRepository.findById(requestId).orElseThrow();
        assertEquals(1, joinGameRequestRepository.count());
        assertFalse(joinGameRequest.isSoloTeam());
        assertEquals("testTeam", joinGameRequest.getTeam().getTeamName());
    }

    @Test
    void 팀으로_게임에_참여하는_요청을_보내고_다른팀으로_다시_요청을_보냄() {
        Game game = gameRepository.findById(initGame()).orElseThrow();
        SubTeamRegistrationParams subTeamRegistrationParams = SubTeamRegistrationParams.builder()
                .teamId(testTeam.getId())
                .user(testUser)
                .matchTeamSide(MatchTeamSide.HOME)
                .gameId(game.getId())
                .build();

        Long requestId = soloJoinGameRequestService.createSubTeamRequest(subTeamRegistrationParams);
        JoinGameRequest joinGameRequest = joinGameRequestRepository.findById(requestId).orElseThrow();
        assertEquals(1, joinGameRequestRepository.count());
        assertFalse(joinGameRequest.isSoloTeam());
        assertEquals("testTeam", joinGameRequest.getTeam().getTeamName());

        SubTeamRegistrationParams subTeamRegistrationParams2 = SubTeamRegistrationParams.builder()
                .teamId(testTeam2.getId())
                .user(testUser)
                .matchTeamSide(MatchTeamSide.HOME)
                .gameId(game.getId())
                .build();
        Long requestId2 = soloJoinGameRequestService.createSubTeamRequest(subTeamRegistrationParams2);
        JoinGameRequest joinGameRequest2 = joinGameRequestRepository.findById(requestId2).orElseThrow();

        assertEquals(1, joinGameRequestRepository.count());
        assertFalse(joinGameRequest2.isSoloTeam());
        assertEquals("testTeam2", joinGameRequest2.getTeam().getTeamName());
    }

    @Test
    void 개인으로_게임에_참여하는_요청을_보냄() {
        Game game = gameRepository.findById(initGame()).orElseThrow();
        SubTeamRegistrationParams subTeamRegistrationParams = SubTeamRegistrationParams.builder()
                .user(testUser)
                .matchTeamSide(MatchTeamSide.HOME)
                .gameId(game.getId())
                .build();

        Long requestId = soloJoinGameRequestService.generateJoinRequest(subTeamRegistrationParams);
        JoinGameRequest joinGameRequest = joinGameRequestRepository.findById(requestId).orElseThrow();
        assertEquals(1, joinGameRequestRepository.count());
        assertTrue(joinGameRequest.isSoloTeam());
        assertEquals("test", joinGameRequest.getUser().getUsername());
    }

    @Test
    void 개인으로_게임에_참여하는_요청을_보내고_다시_요청을_보냄() {
        Game game = gameRepository.findById(initGame()).orElseThrow();
        SubTeamRegistrationParams subTeamRegistrationParams = SubTeamRegistrationParams.builder()
                .user(testUser)
                .matchTeamSide(MatchTeamSide.HOME)
                .gameId(game.getId())
                .build();

        Long requestId = soloJoinGameRequestService.generateJoinRequest(subTeamRegistrationParams);
        JoinGameRequest joinGameRequest = joinGameRequestRepository.findById(requestId).orElseThrow();
        assertEquals(1, joinGameRequestRepository.count());
        assertTrue(joinGameRequest.isSoloTeam());
        assertEquals("test", joinGameRequest.getUser().getUsername());


        SubTeamRegistrationParams subTeamRegistrationParams2 = SubTeamRegistrationParams.builder()
                .user(testUser)
                .matchTeamSide(MatchTeamSide.HOME)
                .gameId(game.getId())
                .build();
        Long requestId2 = soloJoinGameRequestService.generateJoinRequest(subTeamRegistrationParams2);
        JoinGameRequest joinGameRequest2 = joinGameRequestRepository.findById(requestId2).orElseThrow();

        assertEquals(1, joinGameRequestRepository.count());
        assertTrue(joinGameRequest2.isSoloTeam());
        assertNotEquals(joinGameRequest.getRequestTime(), joinGameRequest2.getRequestTime());
    }*/
}