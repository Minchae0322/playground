package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.TeamRegistration;
import com.example.playgroundmanage.dto.response.TeamInfoResponse;
import com.example.playgroundmanage.dto.response.TeamMemberDto;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.game.repository.TeamingRepository;
import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.game.vo.Teaming;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.game.repository.TeamRepository;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.store.UploadFile;
import com.example.playgroundmanage.util.TeamValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.playgroundmanage.util.TeamValidation.validateJoinTeam;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final TeamingService teamingService;


    private final UserService userService;

    private final FileHandler fileHandler;

    private final TeamingRepository teamingRepository;


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
    public List<TeamMemberDto> getTeamMembers(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotExistException::new);
        List<Teaming> teamUserRelations = team.getMembers();

        return teamUserRelations.stream()
                .map(t -> TeamMemberDto.builder()
                        .userProfileImg(getUserProfileImg(t.getUser().getProfileImg()))
                        .userNickname(t.getUser().getNickname())
                        .userRole(t.getRole())
                        .userId(t.getUser().getId())
                        .build())
                .toList();
    }

    public InMemoryMultipartFile getUserProfileImg(UploadFile uploadFile) {
        return Optional.ofNullable(uploadFile)
                .map(fileHandler::extractFile)
                .orElse(null);
    }

    @Transactional
    public String getUserRoleInTeam(User user, Team team) {
        Teaming teaming = teamingRepository.findByTeamAndUser(team, user);
        return teaming.getRole();
    }

    public Team findByTeamId(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(TeamNotExistException::new);
    }


    @Transactional
    public TeamInfoResponse getTeamInfo(Long teamId) throws IOException {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotExistException::new);
        InMemoryMultipartFile teamProfileImg = null;
        if(team.getTeamPic() != null) {
            teamProfileImg = fileHandler.extractFile(team.getTeamPic());
        }
        return TeamInfoResponse.builder()
                .teamName(team.getTeamName())
                .sportsEvent(team.getSportsEvent().getValue())
                .leaderId(team.getLeader().getId())
                .teamProfileImg(teamProfileImg)
                .leaderName(team.getLeader().getNickname())
                .build();
    }


    @Transactional
    public void joinTeam(Long teamId, User user) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotExistException::new);

        validateJoinTeam(team, user);

        teamingRepository.save(Teaming.builder()
                .team(team)
                .user(user)
                .role("Member")
                .build());
    }
}
