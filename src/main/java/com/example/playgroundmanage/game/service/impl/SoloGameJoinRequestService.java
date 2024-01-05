package com.example.playgroundmanage.game.service.impl;

import com.example.playgroundmanage.dto.GameRequestDto;
import com.example.playgroundmanage.dto.response.PendingGameRequest;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.UserNotExistException;
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


    private final SoloGameJoinRequestRepository soloGameJoinRequestRepository;
    private final TeamSelector teamSelector;

    private final SubTeamRepository subTeamRepository;




    private final GameManagementService gameManagementService;

    @Override
    public String getRequestType() {
        return "soloGameJoin";
    }

    @Transactional
    @Override
    public Long generateRequest(Long gameId, GameRequestDto gameRequestDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        validateDuplicateUserInGame(gameManagementService.findGameParticipantsInGame(game), gameRequestDto.getUser());
        gameManagementService.deletePreviousRequest(game, gameRequestDto.getUser());

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
                        .matchTeamSide(gameRequestDto.getMatchTeamSide())
                        .build())
                .getId();
    }



    @Override
    @Transactional
    public Long acceptRequest(Long requestId) {
        SoloGameJoinRequest soloGameJoinRequest = (SoloGameJoinRequest) gameRequestRepository.findById(requestId)
                .orElseThrow();

        SubTeam soloTeam = getSoloTeam(soloGameJoinRequest.getGame(), soloGameJoinRequest.getMatchTeamSide());

        validateDuplicateUserInGame(gameManagementService.findGameParticipantsInGame(soloGameJoinRequest.getGame()), soloGameJoinRequest.getUser());
        gameManagementService.deleteRequest(soloGameJoinRequest.getId());

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

}
