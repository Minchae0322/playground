package com.example.playgroundmanage.game.service.impl;

import com.example.playgroundmanage.dto.JoinGameRequestDto;
import com.example.playgroundmanage.dto.SubTeamRequest;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.GameRequestManagementService;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.game.vo.impl.TeamGameRegistrationRequest;
import com.example.playgroundmanage.game.vo.impl.TeamGameRequest;
import com.example.playgroundmanage.util.TeamSelector;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.playgroundmanage.util.GameValidation.validateDuplicateUserInGame;

@RequiredArgsConstructor
@Service
public class TeamGameRegistrationRequestService implements RequestService {

    private final GameRepository gameRepository;

    private final TeamRepository teamRepository;

    private final GameRequestManagementService gameRequestManagementService;

    private final TeamSelector teamSelector;

    private final SubTeamRepository subTeamRepository;

    private final GameJoinRequestRepository gameJoinRequestRepository;

    @Override
    @Transactional
    public Long generateRequest(Long gameId, JoinGameRequestDto joinGameRequestDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        validateDuplicateUserInGame(gameRequestManagementService.findGameParticipantsInGame(game), joinGameRequestDto.getUser());
        gameRequestManagementService.deletePreviousRequest(game, joinGameRequestDto.getUser());

        return saveJoinRequest(game, joinGameRequestDto);
    }


    @Transactional
    private Long saveJoinRequest(Game game, JoinGameRequestDto joinGameRequestDto) {
        Team team = teamRepository.findById(joinGameRequestDto.getTeamId())
                .orElseThrow(TeamNotExistException::new);

        return gameJoinRequestRepository.save(TeamGameRegistrationRequest.builder()
                        .game(game)
                        .team(team)
                        .user(joinGameRequestDto.getUser())
                        .expiredTime(game.getGameStartDateTime().plusMinutes(game.getRunningTime()))
                        .matchTeamSide(joinGameRequestDto.getMatchTeamSide())
                        .build())
                .getId();
    }





    @Override
    public String getRequestType() {
        return "teamGameRegistration";
    }
}
