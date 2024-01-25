package com.example.playgroundmanage.request.service.impl;

import com.example.playgroundmanage.dto.GameRequestDto;
import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.dto.RequestDto;
import com.example.playgroundmanage.dto.reqeust.PendingRequestParams;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.RequestNotExistException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.GameManagementService;
import com.example.playgroundmanage.request.service.RequestService;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.request.vo.impl.SoloGameJoinRequest;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.playgroundmanage.util.GameValidation.validateDuplicateUserInGame;

@Service
@RequiredArgsConstructor
public class SoloGameJoinRequestService implements RequestService {

    private final GameRepository gameRepository;


    private final GameParticipantRepository gameParticipantRepository;

    private final GameRequestRepository gameRequestRepository;


    private final GameManagementService gameManagementService;

    @Override
    public String getRequestType() {
        return "soloGameJoin";
    }

    @Override
    public List<RequestInfoDto> getPendingRequests(PendingRequestParams pendingRequestParams) {
        List<GameRequest> gameRequests = gameRequestRepository.findAllByHostAndExpiredTimeAfter(pendingRequestParams.getHost(), LocalDateTime.now());
        List<SoloGameJoinRequest> soloGameJoinRequests = gameRequests.stream()
                .filter(gameRequest -> gameRequest instanceof SoloGameJoinRequest)
                .map(gameRequest -> (SoloGameJoinRequest) gameRequest)
                .toList();

        return soloGameJoinRequests.stream()
                .map(SoloGameJoinRequest::toGameRequestInfoDto)
                .toList();
    }

    @Transactional
    @Override
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
        return gameRequestRepository.save(SoloGameJoinRequest.builder()
                        .game(game)
                        .host(game.getHost())
                        .requestTime(gameRequestDto.getRequestTime().getLocalDateTime())
                        .expiredTime(game.getGameStartDateTime())
                        .user(gameRequestDto.getUser())
                        .expiredTime(game.getGameStartDateTime().plusMinutes(game.getRunningTime()))
                        .gameTeamSide(gameRequestDto.getGameTeamSide())
                        .build())
                .getId();
    }



    @Override
    @Transactional
    public Long acceptRequest(Long requestId) {
        SoloGameJoinRequest soloGameJoinRequest = (SoloGameJoinRequest) gameRequestRepository.findById(requestId)
                .orElseThrow(RequestNotExistException::new);

        SubTeam soloTeam = getSoloTeam(soloGameJoinRequest.getGame(), soloGameJoinRequest.getGameTeamSide());

        validateDuplicateUserInGame(gameManagementService.findGameParticipantsInGame(soloGameJoinRequest.getGame()), soloGameJoinRequest.getUser());
        gameManagementService.deleteRequest(soloGameJoinRequest.getId());

        return gameParticipantRepository.save(GameParticipant.builder()
                .isAccepted(true)
                .subTeam(soloTeam)
                .game(soloGameJoinRequest.getGame())
                .user(soloGameJoinRequest.getUser())
                .build()).getId();
    }

    @Override
    public void declineRequest(Long requestId) {
        gameManagementService.deleteRequest(requestId);
    }


    private SubTeam getSoloTeam(Game game, GameTeamSide gameTeamSide) {
        CompetingTeam competingTeam = game.getCompetingTeamBySide(gameTeamSide).orElseThrow();
        return competingTeam.getSoloTeam();
    }

}
