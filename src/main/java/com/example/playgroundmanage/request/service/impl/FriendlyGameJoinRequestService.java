package com.example.playgroundmanage.request.service.impl;

import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.game.dto.GameRequestDto;
import com.example.playgroundmanage.dto.RequestDto;
import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.dto.reqeust.PendingRequestParams;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.RequestNotExistException;
import com.example.playgroundmanage.game.repository.GameParticipantRepository;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.GameRequestRepository;
import com.example.playgroundmanage.request.dto.AthleticsJoinRequest;
import com.example.playgroundmanage.request.repository.AthleticsRequestRepository;
import com.example.playgroundmanage.request.service.RequestProcessor;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.request.service.RequestService;
import com.example.playgroundmanage.request.vo.AthleticsRequest;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.request.vo.impl.FriendlyGameJoinRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.playgroundmanage.util.GameValidation.validateDuplicateUserInGame;

@Service
@RequiredArgsConstructor
public class FriendlyGameJoinRequestService implements RequestService {

    private final GameRepository gameRepository;

    private final GameRequestRepository gameRequestRepository;

    private final RequestProcessor requestProcessor;

    private final GameParticipantRepository gameParticipantRepository;

    @Override
    public String getRequestType() {
        return "friendlyGameJoin";
    }

    @Override
    public Long generateRequest(RequestDto requestDto) {
        GameRequestDto gameRequestDto = (GameRequestDto) requestDto;

        Game game = gameRepository.findById(gameRequestDto.getGameId())
                .orElseThrow(GameNotExistException::new);

        validateDuplicateUserInGame(requestProcessor.findGameParticipantsInGame(game), gameRequestDto.getUser());
        requestProcessor.deletePreviousGameRequest(game, gameRequestDto.getUser());

        return saveJoinRequest(game, gameRequestDto);
    }



    @Transactional
    private Long saveJoinRequest(Game game, GameRequestDto gameRequestDto) {
        return gameRequestRepository.save(FriendlyGameJoinRequest.builder()
                        .game(game)
                        .host(game.getHost())
                        .gameTeamSide(gameRequestDto.getGameTeamSide())
                        .gameTeamSide(gameRequestDto.getGameTeamSide())
                        .requestTime(gameRequestDto.getRequestTime().getLocalDateTime())
                        .expiredTime(game.getGameStartDateTime())
                        .user(gameRequestDto.getUser())
                        .expiredTime(game.getGameStartDateTime().plusMinutes(game.getRunningTime()))
                        .build())
                .getId();
    }



    @Override
    public List<RequestInfoDto> getPendingRequests(PendingRequestParams pendingRequestParams) {
        List<GameRequest> gameRequests = gameRequestRepository.findAllByHostAndExpiredTimeAfter(
                        pendingRequestParams.getHost(),
                        LocalDateTime.now()
                );

        List<FriendlyGameJoinRequest> friendlyGameJoinRequests = gameRequests.stream()
                .filter(gameRequest -> gameRequest instanceof FriendlyGameJoinRequest)
                .map(gameRequest -> (FriendlyGameJoinRequest) gameRequest)
                .toList();

        return friendlyGameJoinRequests.stream()
                .map(FriendlyGameJoinRequest::toGameRequestInfoDto)
                .toList();
    }

    @Override
    public Long acceptRequest(Long requestId) {
        FriendlyGameJoinRequest friendlyGameJoinRequest = (FriendlyGameJoinRequest) gameRequestRepository.findById(requestId)
                .orElseThrow(RequestNotExistException::new);

        validateDuplicateUserInGame(requestProcessor.findGameParticipantsInGame(friendlyGameJoinRequest.getGame()), friendlyGameJoinRequest.getUser());
        requestProcessor.deleteRequest(friendlyGameJoinRequest.getId());

        return gameParticipantRepository.save(GameParticipant.builder()
                .isAccepted(true)
                .game(friendlyGameJoinRequest.getGame())
                .user(friendlyGameJoinRequest.getUser())
                .build()).getId();
    }

    @Override
    public void declineRequest(Long requestId) {
        requestProcessor.deleteRequest(requestId);
    }
}
