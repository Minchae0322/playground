package com.example.playgroundmanage.game.service.impl;

import com.example.playgroundmanage.dto.JoinGameRequestDto;
import com.example.playgroundmanage.dto.response.PendingJoinRequest;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.GameManagementService;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.game.vo.impl.SoloGameJoinRequest;
import com.example.playgroundmanage.type.MatchTeamSide;
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

    private final GameRequestRepository gameRequestRepository;



    private final TeamSelector teamSelector;

    private final SubTeamRepository subTeamRepository;


    private final GameManagementService gameManagementService;

    @Override
    public String getRequestType() {
        return "SoloGameJoin";
    }

    @Transactional
    @Override
    public Long generateRequest(Long gameId, JoinGameRequestDto joinGameRequestDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        validateDuplicateUserInGame(gameManagementService.findGameParticipantsInGame(game), joinGameRequestDto.getUser());
        gameManagementService.deletePreviousRequest(game, joinGameRequestDto.getUser());

        return saveJoinRequest(game, joinGameRequestDto);

    }

    @Transactional
    private Long saveJoinRequest(Game game, JoinGameRequestDto joinGameRequestDto) {
        return gameRequestRepository.save(SoloGameJoinRequest.builder()
                        .game(game)
                        .expiredTime(game.getGameStartDateTime())
                        .user(joinGameRequestDto.getUser())
                        .expiredTime(game.getGameStartDateTime().plusMinutes(game.getRunningTime()))
                        .matchTeamSide(joinGameRequestDto.getMatchTeamSide())
                        .build())
                .getId();
    }



    @Override
    @Transactional
    public Long acceptRequest(Long requestId) {
        SoloGameJoinRequest soloGameJoinRequest = (SoloGameJoinRequest) gameRequestRepository.findById(requestId)
                .orElseThrow();

        SubTeam soloTeam = getSoloTeam(soloGameJoinRequest.getGame(), soloGameJoinRequest.getMatchTeamSide());

        return gameParticipantRepository.save(GameParticipant.builder()
                .isAccepted(true)
                .subTeam(soloTeam)
                .user(soloGameJoinRequest.getUser())
                .build()).getId();
    }

    @Override
    public void declineRequest(Long requestId) {
        gameManagementService.deleteRequest(requestId);
    }


    private SubTeam getSoloTeam(Game game, MatchTeamSide matchTeamSide) {
        CompetingTeam competingTeam = game.getCompetingTeamBySide(matchTeamSide);
        return competingTeam.getSoloTeam();
    }


    public List<PendingJoinRequest> getPendingJoinRequest(User user) {
        List<GameRequest> gameRequests = gameRequestRepository.findAllByUserAndExpiredTimeAfter(user, LocalDateTime.now());
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
