package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.TeamRegistration;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.repository.TeamRepository;
import com.example.playgroundmanage.type.MatchResult;
import com.example.playgroundmanage.vo.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final TeamingService teamingService;

    private final MatchService matchService;

    private final UserService userService;

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

    public boolean isJoinTeamTheMatch(Long matchId, Long teamId, boolean isHome) {
        Match match = matchService.getMatch(matchId);
        if(isHome) {
            return match.getHomeTeam().isContainTeam(teamId);
        }
        return match.getAwayTeam().isContainTeam(teamId);
    }

    public boolean 내가_속한팀이_이미_팀대기열에_있는지(MatchTeam matchTeam, User user) {
        List<Team> myTeams = userService.getTeamUserBelong(user);
        return myTeams.stream()
                .filter(t -> matchTeam.isContainTeam(t.getId()))
                .toList().size() != 0;
    }

    public Integer getTeamMatchResultNumbers(Long teamId, MatchResult matchResult) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotExistException::new);
        List<MatchParticipantTeam> matchParticipantTeams = matchService.getTeamParticipatedMatch(team);

        return matchParticipantTeams.stream()
                .filter(m -> m.getMatchTeam().getMatchResult() == matchResult)
                .toList().size();
    }



}
