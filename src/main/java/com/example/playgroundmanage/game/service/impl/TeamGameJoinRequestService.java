package com.example.playgroundmanage.game.service.impl;

import com.example.playgroundmanage.dto.JoinGameRequestDto;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.GameManagementService;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.game.vo.impl.SoloGameJoinRequest;
import com.example.playgroundmanage.game.vo.impl.TeamGameJoinRequest;
import com.example.playgroundmanage.util.TeamSelector;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Long generateRequest(Long gameId, JoinGameRequestDto joinGameRequestDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        validateDuplicateUserInGame(gameManagementService.findGameParticipantsInGame(game), joinGameRequestDto.getUser());
        gameManagementService.deletePreviousRequest(game, joinGameRequestDto.getUser());

        return saveJoinRequest(game, joinGameRequestDto);
    }

    @Transactional
    private Long saveJoinRequest(Game game, JoinGameRequestDto joinGameRequestDto) {
        SubTeam subTeam = subTeamRepository.findById(joinGameRequestDto.getSubTeamId())
                .orElseThrow();

        return gameRequestRepository.save(TeamGameJoinRequest.builder()
                        .game(game)
                        .subTeam(subTeam)
                        .user(joinGameRequestDto.getUser())
                        .expiredTime(game.getGameStartDateTime().plusMinutes(game.getRunningTime()))
                        .matchTeamSide(joinGameRequestDto.getMatchTeamSide())
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
