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
import com.example.playgroundmanage.request.vo.impl.TeamGameJoinRequest;
import com.example.playgroundmanage.location.respository.TeamRepository;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.team.TeamSelector;
import com.example.playgroundmanage.team.TeamValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.playgroundmanage.util.GameValidation.validateDuplicateUserInGame;

@Service
@RequiredArgsConstructor
public class TeamGameJoinRequestService implements RequestService {

    private final GameRepository gameRepository;

    private final TeamRepository teamRepository;

    private final GameParticipantRepository gameParticipantRepository;

    private final TeamSelector teamSelector;

    private final GameManagementService gameManagementService;


    private final SubTeamRepository subTeamRepository;

    private final GameRequestRepository gameRequestRepository;


    @Override
    public String getRequestType() {
        return "teamGameJoin";
    }

    @Override
    public List<RequestInfoDto> getPendingRequests(PendingRequestParams pendingRequestParams) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<GameRequest> gameRequests = gameRequestRepository.findAllByHostAndExpiredTimeAfter(pendingRequestParams.getHost(), currentTime);

        return gameRequests.stream()
                .filter(TeamGameJoinRequest.class::isInstance)
                .map(TeamGameJoinRequest.class::cast)
                .map(TeamGameJoinRequest::toGameRequestInfoDto)
                .toList();
    }

    @Override
    public Long generateRequest(RequestDto requestDto) {
        GameRequestDto gameRequestDto = (GameRequestDto) requestDto;
        Game game = gameRepository.findById(gameRequestDto.getGameId())
                .orElseThrow(GameNotExistException::new);
        Team team = teamRepository.findById(((GameRequestDto) requestDto).getTeamId())
                .orElseThrow(TeamNotExistException::new);

        TeamValidation.validateUserInTeam(team, requestDto.getUser());
        validateDuplicateUserInGame(gameManagementService.findGameParticipantsInGame(game), gameRequestDto.getUser());
        gameManagementService.deletePreviousGameRequest(game, gameRequestDto.getUser());

        return saveJoinRequest(game, gameRequestDto);
    }

    @Transactional
    private Long saveJoinRequest(Game game, GameRequestDto gameRequestDto) {
        SubTeam subTeam = subTeamRepository.findById(gameRequestDto.getSubTeamId())
                .orElseThrow();

        return gameRequestRepository.save(TeamGameJoinRequest.builder()
                        .game(game)
                        .subTeam(subTeam)
                        .host(game.getHost())
                        .requestTime(gameRequestDto.getRequestTime().getLocalDateTime())
                        .user(gameRequestDto.getUser())
                        .expiredTime(game.getGameStartDateTime().plusMinutes(game.getRunningTime()))
                        .gameTeamSide(gameRequestDto.getGameTeamSide())
                        .build())
                .getId();
    }




    @Override
    @Transactional
    public Long acceptRequest(Long requestId) {
        TeamGameJoinRequest teamGameJoinRequest = (TeamGameJoinRequest) gameRequestRepository.findById(requestId)
                .orElseThrow(RequestNotExistException::new);

        SubTeam subTeam = teamGameJoinRequest.getSubTeam();

        validateDuplicateUserInGame(gameManagementService.findGameParticipantsInGame(teamGameJoinRequest.getGame()), teamGameJoinRequest.getUser());
        gameManagementService.deleteRequest(teamGameJoinRequest.getId());

        return gameParticipantRepository.save(GameParticipant.builder()
                .isAccepted(true)
                .subTeam(subTeam)
                .game(teamGameJoinRequest.getGame())
                .user(teamGameJoinRequest.getUser())
                .build()).getId();
    }

    @Override
    public void declineRequest(Long requestId) {
        gameManagementService.deleteRequest(requestId);
    }


}
