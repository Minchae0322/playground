package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.SubTeamRegistrationParams;
import com.example.playgroundmanage.dto.response.PendingJoinRequest;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.game.match.GameRequestProcess;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.JoinGameRequestRepository;
import com.example.playgroundmanage.game.repository.MatchParticipantRepository;
import com.example.playgroundmanage.game.repository.TeamRepository;
import com.example.playgroundmanage.type.MatchTeamSide;
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

    private final MatchParticipantRepository matchParticipantRepository;

    private final JoinGameRequestRepository joinGameRequestRepository;

    private final GameRequestProcess gameRequestProcess;

    @Transactional
    public Long createGameTeamJoinRequest(SubTeamRegistrationParams subTeamRegistrationParams) {
        Game game = gameRepository.findById(subTeamRegistrationParams.getGameId()).orElseThrow(GameNotExistException::new);
        Team team = teamRepository.findById(subTeamRegistrationParams.getTeamId()).orElseThrow(TeamNotExistException::new);

        if (gameRequestProcess.isUserParticipatingInGame(game, subTeamRegistrationParams.getUser())) {
            throw new IllegalArgumentException();
        }
        return saveJoinTeamRequest(game, subTeamRegistrationParams.getUser(), team, subTeamRegistrationParams.getMatchTeamSide());
    }

    @Transactional
    public Long createGameSoloJoinRequest(SubTeamRegistrationParams subTeamRegistrationParams) {
        Game game = gameRepository.findById(subTeamRegistrationParams.getGameId()).orElseThrow(GameNotExistException::new);
        if (gameRequestProcess.isUserParticipatingInGame(game, subTeamRegistrationParams.getUser())) {
            throw new IllegalArgumentException();
        }
        return saveJoinSoloRequest(game, subTeamRegistrationParams.getUser(), subTeamRegistrationParams.getMatchTeamSide());

    }

    @Transactional
    private Long saveJoinTeamRequest(Game game, User user, Team team, MatchTeamSide matchTeamSide) {
        return joinGameRequestRepository.save(joinGameRequestRepository.findByGameAndUser(game, user).orElse(JoinGameRequest.builder()
                .isSoloTeam(false)
                .game(game)
                .team(team)
                .matchTeamSide(matchTeamSide)
                .expiredTime(game.getMatchStart())
                .user(user)
                .build()).update(team, matchTeamSide)).getId();
    }

    @Transactional
    private Long saveJoinSoloRequest(Game game, User user, MatchTeamSide matchTeamSide) {
        return joinGameRequestRepository.save(joinGameRequestRepository.findByGameAndUser(game, user).orElse(JoinGameRequest.builder()
                .isSoloTeam(true)
                .game(game)
                .expiredTime(game.getMatchStart())
                .user(user)
                .matchTeamSide(matchTeamSide)
                .build()).update(matchTeamSide)).getId();
    }

    @Transactional
    public Long acceptRequest(Long joinGameRequestId) {
        JoinGameRequest joinGameRequest = joinGameRequestRepository.findById(joinGameRequestId).orElseThrow();
        if (joinGameRequest.isSoloTeam()) {
            SubTeam soloTeam = gameRequestProcess.getSoloTeam(joinGameRequest.getGame(), joinGameRequest.getMatchTeamSide());
            return addUserToTeam(soloTeam, joinGameRequest.getUser());
        }
        SubTeam subTeam = gameRequestProcess.getTeam(joinGameRequest.getGame(), joinGameRequest.getTeam(), joinGameRequest.getMatchTeamSide());
        return addUserToTeam(subTeam, joinGameRequest.getUser());
    }

    @Transactional
    public void rejectRequest(Long joinGameRequestId) {
        JoinGameRequest joinGameRequest = joinGameRequestRepository.findById(joinGameRequestId).orElseThrow();
        joinGameRequestRepository.delete(joinGameRequest);
    }

    public Long addUserToTeam(SubTeam subTeam, User user) {
        return matchParticipantRepository.save(MatchParticipant.builder()
                .user(user)
                .subTeam(subTeam)
                .isAccepted(true)
                .build()).getId();
    }


    public List<PendingJoinRequest> getPendingJoinRequest(User user) {
        List<JoinGameRequest> joinGameRequests = joinGameRequestRepository.findAllByUserAndExpiredTimeAfter(user, LocalDateTime.now());
        return joinGameRequests.stream()
                .map(request -> PendingJoinRequest.builder()
                        .teamName(request.getTeamNameOrSoloTeam())
                        .requestId(request.getId())
                        .gameSide(request.getMatchTeamSide().getValue())
                        .requestTime(request.getRequestTime())
                        .username(request.getUser().getUsername())
                        .gameId(request.getGame().getId())
                        .build()
                ).toList();
    }
}