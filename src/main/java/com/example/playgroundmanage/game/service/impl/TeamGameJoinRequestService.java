package com.example.playgroundmanage.game.service.impl;

import com.example.playgroundmanage.dto.JoinGameRequestDto;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.game.vo.impl.TeamGameJoinRequest;
import com.example.playgroundmanage.util.GameValidation;
import com.example.playgroundmanage.util.TeamSelector;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.playgroundmanage.util.GameValidation.validateDuplicateUserInGame;

@Service
@RequiredArgsConstructor
public class TeamGameJoinRequestService implements RequestService {

    private final GameRepository gameRepository;

    private final TeamRepository teamRepository;

    private final GameParticipantRepository gameParticipantRepository;

    private final TeamSelector teamSelector;

    private final SubTeamRepository subTeamRepository;

    private final GameJoinRequestRepository gameJoinRequestRepository;
    @Override
    public Long generateJoinRequest(Long gameId, JoinGameRequestDto joinGameRequestDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        validateDuplicateUserInGame(findGameParticipantsInGame(game), joinGameRequestDto.getUser());
        deletePreviousRequest(game, joinGameRequestDto.getUser());

        return saveJoinRequest(game, joinGameRequestDto);
    }

    @Transactional
    private Long saveJoinRequest(Game game, JoinGameRequestDto joinGameRequestDto) {
        SubTeam subTeam = subTeamRepository.findById(joinGameRequestDto.getSubTeamId())
                .orElseThrow();

        return gameJoinRequestRepository.save(TeamGameJoinRequest.builder()
                        .game(game)
                        .team(subTeam)
                        .expiredTime(game.getGameStartDateTime())
                        .user(joinGameRequestDto.getUser())
                        .expiredTime(game.getGameStartDateTime().plusMinutes(game.getRunningTime()))
                        .matchTeamSide(joinGameRequestDto.getMatchTeamSide())
                        .build())
                .getId();
    }

    private void deletePreviousRequest(Game game, User user) {
        gameJoinRequestRepository.findByGameAndUser(game, user)
                .ifPresent(gameJoinRequestRepository::delete);
    }

    private List<GameParticipant> findGameParticipantsInGame(Game game) {
        return gameParticipantRepository.findAllByGame(game);
    }

    @Override
    public String getRequestType() {
        return "teamGameJoin";
    }







}
