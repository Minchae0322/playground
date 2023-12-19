package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.TeamRegistration;
import com.example.playgroundmanage.game.service.TeamService;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.TeamRepository;
import com.example.playgroundmanage.game.repository.TeamingRepository;
import com.example.playgroundmanage.game.repository.UserRepository;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.game.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

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
        TeamRegistration teamRegistration = TeamRegistration.builder()
                .teamName("testTeam")
                .sportsEvent(SportsEvent.SOCCER)
                .leader(testUser)
                .build();
        Long teamId = teamService.generateTeam(teamRegistration);
        Team team = teamRepository.findById(teamId).orElseThrow();
        assertEquals(1, teamRepository.count());
        assertEquals("test", team.getLeader().getUsername());
        assertEquals(SportsEvent.SOCCER, team.getSportsEvent());
    }

    @Test
    void getMembers() throws IOException {
        TeamRegistration teamRegistration = TeamRegistration.builder()
                .teamName("testTeam")

                .sportsEvent(SportsEvent.SOCCER)
                .leader(testUser)
                .build();
        Long teamId = teamService.generateTeam(teamRegistration);
        Team team = teamRepository.findById(teamId).orElseThrow();
        List<User> userList = teamService.getTeamMembers(team.getId());
        assertEquals(1, userList.size());

    }

    @Test
    void getTeamLeader() throws IOException {
        TeamRegistration teamRegistration = TeamRegistration.builder()
                .teamName("testTeam")
                .sportsEvent(SportsEvent.SOCCER)
                .leader(testUser)
                .build();
        Long teamId = teamService.generateTeam(teamRegistration);
        Team team = teamRepository.findById(teamId).orElseThrow();

        assertEquals("test", team.getLeader().getUsername());

    }

}