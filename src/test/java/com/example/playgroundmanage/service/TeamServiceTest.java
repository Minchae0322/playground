package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.TeamRegistration;
import com.example.playgroundmanage.repository.TeamRepository;
import com.example.playgroundmanage.repository.TeamingRepository;
import com.example.playgroundmanage.repository.UserRepository;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.vo.Team;
import com.example.playgroundmanage.vo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    private User testUser;

    @BeforeEach
    void before() {
        userRepository.deleteAll();
        teamRepository.deleteAll();
        testUser = User.builder()
                .username("test")
                .role(UserRole.USER)
                .isEnable(true)
                .build();
        userRepository.save(testUser);
    }

    @Test
    void generateTeam() {
        TeamRegistration teamRegistration = TeamRegistration.builder()
                .teamName("testTeam")
                .teamPic("teamPic")
                .sportsEvent(SportsEvent.SOCCER)
                .leader(testUser)
                .build();
        Long teamId = teamService.generateTeam(teamRegistration);
        Team team = teamRepository.findById(teamId).orElseThrow();
        assertEquals(1, teamRepository.count());
        assertEquals("test", team.getLeader().getUsername());
        assertEquals(SportsEvent.SOCCER, team.getSportsEvent());
        assertEquals(1, team.getMembers().size());
    }

    @Test
    void getMembers() {
        TeamRegistration teamRegistration = TeamRegistration.builder()
                .teamName("testTeam")
                .teamPic("teamPic")
                .sportsEvent(SportsEvent.SOCCER)
                .leader(testUser)
                .build();
        Long teamId = teamService.generateTeam(teamRegistration);
        Team team = teamRepository.findById(teamId).orElseThrow();
        List<User> userList = teamService.getTeamMembers(team.getId());
        assertEquals(1, userList.size());

    }

    @Test
    void getTeamLeader() {
        TeamRegistration teamRegistration = TeamRegistration.builder()
                .teamName("testTeam")
                .teamPic("teamPic")
                .sportsEvent(SportsEvent.SOCCER)
                .leader(testUser)
                .build();
        Long teamId = teamService.generateTeam(teamRegistration);
        Team team = teamRepository.findById(teamId).orElseThrow();

        assertEquals("test", team.getLeader().getUsername());

    }

}