package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.TeamRegistration;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.repository.TeamRepository;
import com.example.playgroundmanage.vo.Team;
import com.example.playgroundmanage.vo.Teaming;
import com.example.playgroundmanage.vo.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final TeamingService teamingService;

    @Transactional
    public Team saveTeam(TeamRegistration teamRegistration) {
        Team team = Team.builder()
                .teamName(teamRegistration.getTeamName())
                .teamPic(teamRegistration.getTeamPic())
                .leader(teamRegistration.getLeader())
                .sportsEvent(teamRegistration.getSportsEvent())
                .build();
        return teamRepository.save(team);
    }

    public Team generateTeam(TeamRegistration teamRegistration) {
        Team team = saveTeam(teamRegistration);
        teamingService.joinTeam(team, teamRegistration.getLeader());
        return team;
    }

    public List<User> getTeamMembers(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotExistException::new);
        List<Teaming> teamUserRelations = teamingService.getTeamUserRelations(team);

        return teamUserRelations.stream()
                .map(Teaming::getUser)
                .toList();
    }

}
