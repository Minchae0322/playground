package com.example.playgroundmanage.team.service;

import com.example.playgroundmanage.dto.TeamRequestDto;
import com.example.playgroundmanage.team.dto.TeamDto;
import com.example.playgroundmanage.team.dto.TeamMemberDto;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.team.repository.TeamingRepository;
import com.example.playgroundmanage.team.TeamMemberFinder;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.team.vo.Teaming;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.team.repository.TeamRepository;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.vo.UploadFile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.example.playgroundmanage.team.TeamValidation.validateJoinTeam;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    private final TeamingService teamingService;

    private final FileHandler fileHandler;

    private final TeamingRepository teamingRepository;

    private final TeamMemberFinder teamMemberFinder;


    @Transactional
    public Long generateTeam(TeamRequestDto teamRequestDto)  {
        Team team = saveTeam(teamRequestDto);
        teamingService.joinTeam(team, teamRequestDto.getLeader());
        return team.getId();
    }

    @Transactional
    private Team saveTeam(TeamRequestDto teamRequestDto) {
        validateTeamName(teamRequestDto.getTeamName());
        Team team = Team.builder()
                .teamName(teamRequestDto.getTeamName())
                .leader(teamRequestDto.getLeader())
                .description(teamRequestDto.getTeamDescription())
                .sportsEvent(teamRequestDto.getSportsEvent())
                .build();

        if (teamRequestDto.getTeamPic() != null) {
            UploadFile uploadFile = fileHandler.storeFile(teamRequestDto.getTeamPic());
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
    public List<TeamMemberDto> getTeamMembers(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotExistException::new);
        List<Teaming> teamUserRelations = team.getMembers();

        return teamUserRelations.stream()
                .map(t -> TeamMemberDto.builder()
                        .userProfileImg(fileHandler.getExtFullPath(t.getUser().getUserProfileImg().getStoreFileName()))
                        .userNickname(t.getUser().getNickname())
                        .userRole(t.getRole())
                        .userId(t.getUser().getId())
                        .build())
                .toList();
    }



    @Transactional
    public TeamDto.TeamResponseDto getTeamInfo(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotExistException::new);


        return TeamDto.TeamResponseDto.builder()
                .teamName(team.getTeamName())
                .description(team.getDescription())
                .sportsEvent(team.getSportsEvent().getValue())
                .leaderId(team.getLeader().getId())
                .teamProfileImg(fileHandler.getExtFullPath(team.getTeamProfileImg().getStoreFileName()))
                .leaderName(team.getLeader().getNickname())
                .build();
    }


    @Transactional
    public Long joinTeam(Long teamId, User user) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotExistException::new);

        validateJoinTeam(team, user);

        return teamingRepository.save(Teaming.builder()
                .team(team)
                .user(user)
                .createdTime(LocalDate.now())
                .role("Member")
                .build()).getId();
    }

    @Transactional
    public boolean isTeamMember(Long teamId, User user) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotExistException::new);

        return teamMemberFinder.isTeamMember(team, user);
    }

    @Transactional
    public boolean isTeamLeader(Long teamId, User user) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotExistException::new);

        return teamMemberFinder.isTeamLeader(team, user);
    }

    @Transactional
    public void deleteTeam(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotExistException::new);



        teamRepository.delete(team);
    }
}
