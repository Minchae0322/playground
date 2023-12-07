package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.TeamRegistration;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.game.vo.Teaming;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.game.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final TeamingService teamingService;


    private final UserService userService;


    public Team saveTeam(TeamRegistration teamRegistration) {
        Team team = Team.builder()
                .teamName(teamRegistration.getTeamName())
                .teamPic(teamRegistration.getTeamPic())
                .leader(teamRegistration.getLeader())
                .sportsEvent(teamRegistration.getSportsEvent())
                .build();
        return teamRepository.save(team);
    }


    @Transactional
    public Long generateTeam(TeamRegistration teamRegistration) {
        Team team = saveTeam(teamRegistration);
        teamingService.joinTeam(team, teamRegistration.getLeader());
        return team.getId();
    }

    @Transactional
    public List<User> getTeamMembers(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotExistException::new);
        List<Teaming> teamUserRelations = team.getMembers();

        return teamUserRelations.stream()
                .map(Teaming::getUser)
                .toList();
    }

    public Team findByTeamId(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(TeamNotExistException::new);
    }







}