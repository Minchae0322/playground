package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.MatchRegistration;
import com.example.playgroundmanage.repository.*;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.vo.Match;
import com.example.playgroundmanage.vo.User;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MatchServiceTest {
    private  MatchParticipantTeamRepository matchParticipantTeamRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private MatchTeamRepository matchTeamRepository;

    private  MatchParticipantRepository matchParticipantRepository;

    @Autowired
    private MatchService matchService;
    private User testUser;

    @Autowired
    private UserRepository userRepository;
    @BeforeEach
    void before() {
        matchRepository.deleteAll();
        matchTeamRepository.deleteAll();
        testUser = User.builder()
                .username("test")
                .role(UserRole.USER)
                .isEnable(true)
                .build();
        userRepository.save(testUser);
    }

    @Test
    void start_match() {
        MatchRegistration matchRegistration = MatchRegistration.builder()
                .matchStart(LocalDate.now())
                .runningTime(60L)
                .host(testUser)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        Match match = matchService.startMatch(matchRegistration);

        Assertions.assertEquals(1, matchRepository.count());
        Assertions.assertEquals(2L, match.getHomeTeam().getId());
        Assertions.assertEquals(1L, match.getAwayTeam().getId());
        assertEquals("test", match.getHost().getUsername());
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

        List<Match> matches = matchRegistrationList.stream()
                .map(m -> matchService.startMatch(m))
                .toList();

        Assertions.assertEquals(19, matchRepository.count());
        Assertions.assertEquals(18L, matches.get(18).getRunningTime());
        Assertions.assertEquals(38L, matches.get(18).getHomeTeam().getId());
        Assertions.assertEquals(37L, matches.get(18).getAwayTeam().getId());
        Assertions.assertEquals(38, matchTeamRepository.count());
    }

}