package com.example.playgroundmanage.request.service.impl;

import com.example.playgroundmanage.dto.GameRequestDto;
import com.example.playgroundmanage.dto.RequestDto;
import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.dto.reqeust.PendingRequestParams;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.GameRequestRepository;
import com.example.playgroundmanage.game.service.GameManagementService;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.request.service.RequestService;
import com.example.playgroundmanage.request.vo.impl.SoloGameJoinRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.playgroundmanage.util.GameValidation.validateDuplicateUserInGame;

@Service
@RequiredArgsConstructor
public class FriendlyGameJoinRequestService implements RequestService {

    private final GameRepository gameRepository;

    private final GameRequestRepository gameRequestRepository;

    private final GameManagementService gameManagementService;

    @Override
    public String getRequestType() {
        return "FriendlyGameJoin";
    }

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
    public List<RequestInfoDto> getPendingRequests(PendingRequestParams pendingRequestParams) {
        return null;
    }

    @Override
    public Long acceptRequest(Long requestId) {
        return null;
    }

    @Override
    public void declineRequest(Long requestId) {

    }
}
