package com.example.playgroundmanage.request.service.impl;

import com.example.playgroundmanage.exception.RequestNotExistException;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.team.repository.TeamRepository;
import com.example.playgroundmanage.game.repository.TeamRequestRepository;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.dto.PendingRequestResponse;
import com.example.playgroundmanage.request.dto.TeamJoinRequestDto;
import com.example.playgroundmanage.request.service.TeamRequestService;
import com.example.playgroundmanage.request.vo.TeamRequest;
import com.example.playgroundmanage.request.vo.impl.TeamJoinRequest;
import com.example.playgroundmanage.team.service.TeamService;
import com.example.playgroundmanage.team.vo.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class TeamJoinRequestService implements TeamRequestService {

    private final TeamRequestRepository teamRequestRepository;

    private final TeamService teamService;

    private final TeamRepository teamRepository;

    private final UserRepository userRepository;

    @Override
    public String getRequestType() {
        return "teamJoin";
    }

    @Override
    public Long generateRequest(Long userId, TeamJoinRequestDto teamJoinRequestDto) {
        User requestUser = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        Team team = teamRepository.findById(teamJoinRequestDto.teamId())
                .orElseThrow(TeamNotExistException::new);

        TeamRequest teamJoinRequest = findPreviousRequest(requestUser, team)
                .orElse(TeamJoinRequest.of(
                        requestUser,
                        team.getLeader(),
                        team,
                        teamJoinRequestDto.introduction()
                )).update(teamJoinRequestDto.introduction());

        return teamRequestRepository.save(teamJoinRequest).getId();
    }

    private Optional<TeamRequest> findPreviousRequest(User requestUser, Team team) {
        return teamRequestRepository.findByTeamAndUser(team, requestUser);
    }

    @Override
    public Long acceptRequest(Long requestId) {
        TeamJoinRequest teamJoinRequest = (TeamJoinRequest) teamRequestRepository.findById(requestId)
                .orElseThrow(RequestNotExistException::new);

        return teamService.joinTeam(teamJoinRequest.getTeam().getId(), teamJoinRequest.getUser());
    }

    @Override
    public List<PendingRequestResponse> getPendingRequests(Long hostId) {
        User leader = userRepository.findById(hostId)
                .orElseThrow(UserNotExistException::new);

        List<TeamJoinRequest> teamJoinRequests = teamRequestRepository.findAllByLeader(leader);
        return teamJoinRequests.stream()
                .map(PendingRequestResponse::of)
                .toList();
    }

    @Override
    public void rejectRequest(Long requestId) {
        teamRequestRepository.findById(requestId)
                .ifPresent(teamRequestRepository::delete);
    }


/*    @Override
    @Transactional
    public Long generateRequest(RequestDto requestDto) {
        TeamJoinRequestDto teamJoinRequestDto = (TeamJoinRequestDto) requestDto;
        Team team = teamRepository.findById(teamJoinRequestDto.getTeamId())
                .orElseThrow(TeamNotExistException::new);

        validateJoinTeam(team, requestDto.getUser());
        requestProcessor.deletePreviousTeamRequest(team, requestDto.getUser());

        return saveJoinRequest(team, teamJoinRequestDto);
    }

    @Transactional
    private Long saveJoinRequest(Team team, TeamJoinRequestDto teamJoinRequestDto) {
        return teamRequestRepository.save(TeamJoinRequest.builder()
                        .user(teamJoinRequestDto.getUser())
                        .requestTime(teamJoinRequestDto.getRequestTime().getLocalDateTime())
                        .introduction(teamJoinRequestDto.getIntroduction())
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
    @Version
    public Long acceptRequest(Long requestId) {
        TeamRequest teamRequest = (TeamRequest) teamRequestRepository.findById(requestId)
                .orElseThrow(RequestNotExistException::new);
        teamRequestRepository.delete(teamRequest);

        return teamService.joinTeam(teamRequest.getTeam().getId(), teamRequest.getUser());
    }

    @Override
    @Transactional
    public void declineRequest(Long requestId) {
        teamRequestRepository.findById(requestId).ifPresent(teamRequestRepository::delete);
    }*/


}
