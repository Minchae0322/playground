package com.example.playgroundmanage.game.service.impl;

import com.example.playgroundmanage.dto.JoinGameRequestDto;
import com.example.playgroundmanage.dto.response.PendingJoinRequest;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.GameRequestManagementService;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.game.vo.impl.SoloGameRequest;
import com.example.playgroundmanage.util.GameValidation;
import com.example.playgroundmanage.util.TeamSelector;
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

    private final TeamRepository teamRepository;

    private final GameParticipantRepository gameParticipantRepository;

    private final GameJoinRequestRepository gameJoinRequestRepository;



    private final TeamSelector teamSelector;

    private final SubTeamRepository subTeamRepository;


    private final GameRequestManagementService gameRequestManagementService;

    @Transactional
    @Override
    public Long generateRequest(Long gameId, JoinGameRequestDto joinGameRequestDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);


        validateDuplicateUserInGame(gameRequestManagementService.findGameParticipantsInGame(game), joinGameRequestDto.getUser());
        gameRequestManagementService.deletePreviousRequest(game, joinGameRequestDto.getUser());

        return saveJoinRequest(game, joinGameRequestDto);

    }

    @Transactional
    private Long saveJoinRequest(Game game, JoinGameRequestDto joinGameRequestDto) {
        return gameJoinRequestRepository.save(SoloGameRequest.builder()
                        .game(game)
                        .expiredTime(game.getGameStartDateTime())
                        .user(joinGameRequestDto.getUser())
                        .expiredTime(game.getGameStartDateTime().plusMinutes(game.getRunningTime()))
                        .matchTeamSide(joinGameRequestDto.getMatchTeamSide())
                        .build())
                .getId();
    }

    @Override
    public String getRequestType() {
        return "SoloGameJoin";
    }


    public List<PendingJoinRequest> getPendingJoinRequest(User user) {
        List<GameRequest> gameRequests = gameJoinRequestRepository.findAllByUserAndExpiredTimeAfter(user, LocalDateTime.now());
        return gameRequests.stream()
                .map(request -> PendingJoinRequest.builder()
                        .requestId(request.getId())
                        .gameSide(request.getMatchTeamSide().getValue())
                        .requestTime(request.getRequestTime())
                        .username(request.getUser().getUsername())
                        .gameId(request.getGame().getId())
                        .build()
                ).toList();
    }
}
