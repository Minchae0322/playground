package com.example.playgroundmanage.request.service.impl;

import com.example.playgroundmanage.dto.GameRequestDto;
import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.dto.RequestDto;
import com.example.playgroundmanage.dto.reqeust.PendingRequestParams;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.RequestNotExistException;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.GameManagementService;
import com.example.playgroundmanage.request.service.RequestService;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.request.vo.impl.TeamGameRegistrationRequest;
import com.example.playgroundmanage.location.respository.TeamRepository;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.team.TeamSelector;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.playgroundmanage.util.GameValidation.validateDuplicateUserInGame;

@RequiredArgsConstructor
@Service
public class TeamGameRegistrationRequestService implements RequestService {

    private final GameRepository gameRepository;

    private final TeamRepository teamRepository;

    private final GameManagementService gameManagementService;

    private final TeamSelector teamSelector;

    private final SubTeamRepository subTeamRepository;

    private final GameParticipantRepository gameParticipantRepository;

    private final GameRequestRepository gameRequestRepository;


    @Override
    @Transactional
    public Long generateRequest(RequestDto requestDto) {
        GameRequestDto gameRequestDto = (GameRequestDto) requestDto;
        Game game = gameRepository.findById(gameRequestDto.getGameId())
                .orElseThrow(GameNotExistException::new);

        validateDuplicateUserInGame(gameManagementService.findGameParticipantsInGame(game), gameRequestDto.getUser());
        gameManagementService.deletePreviousGameRequest(game, gameRequestDto.getUser());

        return saveJoinRequest(game, gameRequestDto);
    }


    @Transactional
    private Long saveJoinRequest(Game game, GameRequestDto gameRequestDto) {
        Team team = teamRepository.findById(gameRequestDto.getTeamId())
                .orElseThrow(TeamNotExistException::new);

        return gameRequestRepository.save(TeamGameRegistrationRequest.builder()
                        .game(game)
                        .team(team)
                        .host(game.getHost())
                        .requestTime(gameRequestDto.getRequestTime().getLocalDateTime())
                        .user(gameRequestDto.getUser())
                        .expiredTime(game.getGameStartDateTime().plusMinutes(game.getRunningTime()))
                        .gameTeamSide(gameRequestDto.getGameTeamSide())
                        .build())
                .getId();
    }





    @Override
    public String getRequestType() {
        return "teamGameRegistration";
    }

    @Override
    public List<RequestInfoDto> getPendingRequests(PendingRequestParams pendingRequestParams) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<GameRequest> gameRequests = gameRequestRepository.findAllByHostAndExpiredTimeAfter(pendingRequestParams.getHost(), currentTime);

        return gameRequests.stream()
                .filter(TeamGameRegistrationRequest.class::isInstance)
                .map(TeamGameRegistrationRequest.class::cast)
                .map(TeamGameRegistrationRequest::toGameRequestInfoDto)
                .toList();
    }

    @Override
    @Transactional
    public Long acceptRequest(Long requestId) {
        TeamGameRegistrationRequest teamGameRegistrationRequest = (TeamGameRegistrationRequest) gameRequestRepository.findById(requestId)
                .orElseThrow(RequestNotExistException::new);

        SubTeam subTeam = createSubTeam(teamGameRegistrationRequest.getTeam(), teamGameRegistrationRequest.getGame().getCompetingTeamBySide(teamGameRegistrationRequest.getGameTeamSide()).orElseThrow());

        validateDuplicateUserInGame(gameManagementService.findGameParticipantsInGame(teamGameRegistrationRequest.getGame()), teamGameRegistrationRequest.getUser());
        gameManagementService.deleteRequest(teamGameRegistrationRequest.getId());

        return gameParticipantRepository.save(GameParticipant.builder()
                .isAccepted(true)
                .subTeam(subTeam)
                .game(teamGameRegistrationRequest.getGame())
                .user(teamGameRegistrationRequest.getUser())
                .build()).getId();
    }

    @Transactional
    private SubTeam createSubTeam(Team team, CompetingTeam competingTeam) {
        return subTeamRepository.save(SubTeam.builder()
                .isAccept(true)
                .isSoloTeam(false)
                .competingTeam(competingTeam)
                .team(team)
                .build());
    }

    @Override
    public void declineRequest(Long requestId) {
        gameManagementService.deleteRequest(requestId);
    }
}
