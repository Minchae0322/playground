package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.TeamRegistration;
import com.example.playgroundmanage.login.service.UserService;
import com.example.playgroundmanage.repository.TeamRepository;
import com.example.playgroundmanage.repository.UserRepository;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.type.UserRole;
import com.example.playgroundmanage.vo.Team;
import com.example.playgroundmanage.vo.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

class TeamServiceTest {



    @Autowired
    private TeamService teamService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;

    private User testUser;

    @BeforeEach
    void before() {
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
        Team team = teamService.generateTeam(teamRegistration);

        assertEquals(1, teamRepository.count());
        assertEquals("test", team.getLeader().getUsername());
        assertEquals(SportsEvent.SOCCER, team.getSportsEvent());
    }

}