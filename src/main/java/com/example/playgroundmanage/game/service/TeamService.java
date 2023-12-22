package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.TeamRegistration;
import com.example.playgroundmanage.dto.response.TeamInfoResponse;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.game.vo.Teaming;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.game.repository.TeamRepository;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.store.UploadFile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final TeamingService teamingService;


    private final UserService userService;

    private final FileHandler fileHandler;


    @Transactional
    public Team saveTeam(TeamRegistration teamRegistration) throws IOException {
        validateTeamName(teamRegistration.getTeamName());
        Team team = Team.builder()
                .teamName(teamRegistration.getTeamName())
                .leader(teamRegistration.getLeader())
                .description(teamRegistration.getTeamDescription())
                .sportsEvent(teamRegistration.getSportsEvent())
                .build();
        if (teamRegistration.getTeamPic() != null) {
            UploadFile uploadFile = fileHandler.storeFile(teamRegistration.getTeamPic());
            team.updateTeamPic(uploadFile);
        }
        return teamRepository.save(team);
    }

    public void validateTeamName(String teamName) {
        if (teamRepository.existsByTeamName(teamName)) {
            throw new IllegalArgumentException("이미 존재하는 팀 이름 입니다.");
        }
    }


    @Transactional
    public Long generateTeam(TeamRegistration teamRegistration) throws IOException {
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


    @Transactional
    public TeamInfoResponse getTeamInfo(Long teamId) throws IOException {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotExistException::new);
        InMemoryMultipartFile teamProfileImg = fileHandler.extractFile(team.getTeamPic());
        return TeamInfoResponse.builder()
                .teamName(team.getTeamName())
                .teamProfileImg(teamProfileImg)
                .sportsEvent(team.getSportsEvent().getValue())
                .leaderId(team.getLeader().getId())
                .leaderName(team.getLeader().getNickname())
                .build();
    }







}
