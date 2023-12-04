package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.SubTeamRegistrationParams;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.match.GameRequestProcess;
import com.example.playgroundmanage.repository.GameRepository;
import com.example.playgroundmanage.repository.JoinGameRequestRepository;
import com.example.playgroundmanage.repository.MatchParticipantRepository;
import com.example.playgroundmanage.repository.TeamRepository;
import com.example.playgroundmanage.vo.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final GameRepository gameRepository;

    private final TeamRepository teamRepository;

    private final JoinGameRequestRepository joinGameRequestRepository;

    private final GameRequestProcess gameRequestProcess;

    @Transactional
    public Long createGameTeamJoinRequest(SubTeamRegistrationParams subTeamRegistrationParams) {
        Game game = gameRepository.findById(subTeamRegistrationParams.getGameId()).orElseThrow(GameNotExistException::new);
        Team team = teamRepository.findById(subTeamRegistrationParams.getTeamId()).orElseThrow(TeamNotExistException::new);

        if (gameRequestProcess.isUserParticipatingInGame(game, subTeamRegistrationParams.getUser())) {
            throw new IllegalArgumentException();
        }
        return saveJoinTeamRequest(game, subTeamRegistrationParams.getUser(), team);
    }

    @Transactional
    public Long createGameSoloJoinRequest(SubTeamRegistrationParams subTeamRegistrationParams) {
        Game game = gameRepository.findById(subTeamRegistrationParams.getGameId()).orElseThrow(GameNotExistException::new);
        if (gameRequestProcess.isUserParticipatingInGame(game, subTeamRegistrationParams.getUser())) {
            throw new IllegalArgumentException();
        }
        return saveJoinSoloRequest(game, subTeamRegistrationParams.getUser());

    }

    @Transactional
    private Long saveJoinTeamRequest(Game game, User user, Team team) {
        return joinGameRequestRepository.save(joinGameRequestRepository.findByGameAndUser(game, user).orElse(JoinGameRequest.builder()
                .isSoloTeam(false)
                .game(game)
                .team(team)
                .expiredTime(game.getMatchStart())
                .user(user)
                .build()).update(team)).getId();
    }

    @Transactional
    private Long saveJoinSoloRequest(Game game, User user) {
        return joinGameRequestRepository.save(joinGameRequestRepository.findByGameAndUser(game, user).orElse(JoinGameRequest.builder()
                .isSoloTeam(true)
                .game(game)
                .expiredTime(game.getMatchStart())
                .user(user)
                .build()).update()).getId();
    }

    public void getAddPendingTeamRequest(User user) {
        joinGameRequestRepository.findAllByUserAndExpiredTimeAfter(user, LocalDateTime.now());
    }

}
