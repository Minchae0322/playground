package com.example.playgroundmanage.game.service.impl;

import com.example.playgroundmanage.dto.GameRequestDto;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.GameManagementService;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.game.vo.impl.TeamGameJoinRequest;
import com.example.playgroundmanage.util.TeamSelector;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
    public Long generateRequest(Long gameId, GameRequestDto gameRequestDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        validateDuplicateUserInGame(gameManagementService.findGameParticipantsInGame(game), gameRequestDto.getUser());
        gameManagementService.deletePreviousRequest(game, gameRequestDto.getUser());

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
                        .matchTeamSide(gameRequestDto.getMatchTeamSide())
                        .build())
                .getId();
    }




    @Override
    @Transactional
    public Long acceptRequest(Long requestId) {
        TeamGameJoinRequest teamGameJoinRequest = (TeamGameJoinRequest) gameRequestRepository.findById(requestId)
                .orElseThrow();

        SubTeam subTeam = teamGameJoinRequest.getSubTeam();

        return gameParticipantRepository.save(GameParticipant.builder()
                .isAccepted(true)
                .subTeam(subTeam)
                .user(teamGameJoinRequest.getUser())
                .build()).getId();
    }

    @Override
    public void declineRequest(Long requestId) {
        gameManagementService.deleteRequest(requestId);
    }


}
