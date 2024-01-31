package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.TeamRequestDto;
import com.example.playgroundmanage.team.service.TeamService;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.location.respository.TeamRepository;
import com.example.playgroundmanage.location.repository.TeamingRepository;
import com.example.playgroundmanage.game.repository.UserRepository;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.game.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class TeamServiceTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;



    @Autowired
    private TeamingRepository teamingRepository;

    @Autowired
    private GameRepository gameRepository;
    private User testUser;

    @BeforeEach
    void before() {
        gameRepository.deleteAll();
        teamRepository.deleteAll();
        userRepository.deleteAll();

        testUser = User.builder()
                .username("test")
                .role(UserRole.USER)
                .isEnable(true)
                .build();
        userRepository.save(testUser);
    }

    @Test
    void generateTeam() throws IOException {
        TeamRequestDto teamRequestDto = TeamRequestDto.builder()
                .teamName("testTeam")
                .sportsEvent(SportsEvent.SOCCER)
                .leader(testUser)
                .build();
        Long teamId = teamService.generateTeam(teamRequestDto);
        Team team = teamRepository.findById(teamId).orElseThrow();
        assertEquals(1, teamRepository.count());
        assertEquals("test", team.getLeader().getUsername());
        assertEquals(SportsEvent.SOCCER, team.getSportsEvent());
    }

    @Test
    void getMembers() throws IOException {
        TeamRequestDto teamRequestDto = TeamRequestDto.builder()
                .teamName("testTeam")

                .sportsEvent(SportsEvent.SOCCER)
                .leader(testUser)
                .build();
        Long teamId = teamService.generateTeam(teamRequestDto);
        Team team = teamRepository.findById(teamId).orElseThrow();


    }

    @Test
    void getTeamLeader() throws IOException {
        TeamRequestDto teamRequestDto = TeamRequestDto.builder()
                .teamName("testTeam")
                .sportsEvent(SportsEvent.SOCCER)
                .leader(testUser)
                .build();
        Long teamId = teamService.generateTeam(teamRequestDto);
        Team team = teamRepository.findById(teamId).orElseThrow();

        assertEquals("test", team.getLeader().getUsername());

    }

}