package com.example.playgroundmanage.request.service.impl;

import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.dto.RequestDto;
import com.example.playgroundmanage.dto.TeamRequestDto;
import com.example.playgroundmanage.dto.reqeust.PendingRequestParams;
import com.example.playgroundmanage.exception.RequestNotExistException;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.location.respository.TeamRepository;
import com.example.playgroundmanage.game.repository.TeamRequestRepository;
import com.example.playgroundmanage.game.service.GameManagementService;
import com.example.playgroundmanage.request.service.RequestService;
import com.example.playgroundmanage.team.service.TeamService;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.request.vo.TeamRequest;
import com.example.playgroundmanage.request.vo.impl.TeamJoinRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.playgroundmanage.team.TeamValidation.validateJoinTeam;


@RequiredArgsConstructor
@Service
public class TeamJoinRequestService implements RequestService {

    private final TeamRepository teamRepository;

    private final TeamRequestRepository teamRequestRepository;

    private final GameManagementService gameManagementService;

    private final TeamService teamService;

    @Override
    @Transactional
    public Long generateRequest(RequestDto requestDto) {
        TeamRequestDto teamRequestDto = (TeamRequestDto) requestDto;
        Team team = teamRepository.findById(teamRequestDto.getTeamId())
                .orElseThrow(TeamNotExistException::new);

        validateJoinTeam(team, requestDto.getUser());
        gameManagementService.deletePreviousTeamRequest(team, requestDto.getUser());

        return saveJoinRequest(team, teamRequestDto);
    }

    @Transactional
    private Long saveJoinRequest(Team team, TeamRequestDto teamRequestDto) {
        return teamRequestRepository.save(TeamJoinRequest.builder()
                        .user(teamRequestDto.getUser())
                        .requestTime(teamRequestDto.getRequestTime().getLocalDateTime())
                        .introduction(teamRequestDto.getIntroduction())
                        .leader(team.getLeader())
                        .team(team)
                        .build())
                .getId();
    }

    @Override
    public String getRequestType() {
        return "teamJoin";
    }

    @Override
    public List<RequestInfoDto> getPendingRequests(PendingRequestParams pendingRequestParams) {
        List<TeamRequest> teamRequests = teamRequestRepository.findAllByLeader(pendingRequestParams.getHost());

        return teamRequests.stream()
                .filter(TeamJoinRequest.class::isInstance)
                .map(TeamJoinRequest.class::cast)
                .map(TeamRequest::toTeamRequestInfoDto)
                .toList();
    }

    @Override
    @Transactional
    public Long acceptRequest(Long requestId) {
        TeamRequest teamRequest = (TeamRequest) teamRequestRepository.findById(requestId)
                .orElseThrow(RequestNotExistException::new);

        return teamService.joinTeam(teamRequest.getId(), teamRequest.getUser());
    }

    @Override
    @Transactional
    public void declineRequest(Long requestId) {
        TeamRequest teamRequest = (TeamRequest) teamRequestRepository.findById(requestId)
                .orElseThrow(RequestNotExistException::new);

        teamRequestRepository.delete(teamRequest);
    }
}