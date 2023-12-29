package com.example.playgroundmanage.game.service.impl;

import com.example.playgroundmanage.dto.JoinGameRequestDto;
import com.example.playgroundmanage.dto.SubTeamRegistrationParams;
import com.example.playgroundmanage.dto.response.PendingJoinRequest;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.game.vo.impl.SoloJoinGameRequest;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.util.GameValidation;
import com.example.playgroundmanage.util.TeamSelector;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SoloGameJoinRequestService implements RequestService {

    private final GameRepository gameRepository;

    private final TeamRepository teamRepository;

    private final GameParticipantRepository gameParticipantRepository;

    private final JoinGameRequestRepository joinGameRequestRepository;



    private final TeamSelector teamSelector;

    private final SubTeamRepository subTeamRepository;

    @Transactional
    public Long createSubTeamRequest(SubTeamRegistrationParams subTeamRegistrationParams) {
        Game game = gameRepository.findById(subTeamRegistrationParams.getGameId()).orElseThrow(GameNotExistException::new);
        Team team = teamRepository.findById(subTeamRegistrationParams.getTeamId()).orElseThrow(TeamNotExistException::new);

        List<GameParticipant> gameParticipants = findGameParticipantsInGame(game);
        GameValidation.validateExistUserInGame(gameParticipants, subTeamRegistrationParams.getUser());
        return saveJoinTeamRequest(game, subTeamRegistrationParams.getUser(), team, subTeamRegistrationParams.getMatchTeamSide());
    }

    @Transactional
    @Override
    public Long generateJoinRequest(Long gameId, JoinGameRequestDto joinGameRequestDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        List<GameParticipant> gameParticipants = findGameParticipantsInGame(game);
        GameValidation.validateExistUserInGame(gameParticipants, joinGameRequestDto.getUser());

        return saveJoinRequest(game, joinGameRequestDto);

    }




    @Transactional
    private Long saveJoinRequest(Game game, JoinGameRequestDto joinGameRequestDto) {
        return joinGameRequestRepository.save(joinGameRequestRepository.findByGameAndUser(game, joinGameRequestDto.getUser())
                .orElse(SoloJoinGameRequest.builder()
                        .game(game)
                        .expiredTime(game.getGameStartDateTime())
                        .user(joinGameRequestDto.getUser())
                        .expiredTime(game.getGameStartDateTime().plusMinutes(game.getRunningTime()))
                        .matchTeamSide(joinGameRequestDto.getMatchTeamSide())
                        .build())
                .update())
                .getId();
    }

    @Override
    public String getRequestType() {
        return "SoloGameJoin";
    }

    public List<GameParticipant> findGameParticipantsInGame(Game game) {
        return gameParticipantRepository.findAllByGame(game);
    }

    private Long saveJoinTeamRequest(Game game, User user, Team team, MatchTeamSide matchTeamSide) {
        return joinGameRequestRepository.save(joinGameRequestRepository.findByGameAndUser(game, user).orElse(JoinGameRequest.builder()
                .isSoloTeam(false)
                .game(game)
                .team(team)
                .matchTeamSide(matchTeamSide)
                .expiredTime(game.getGameStartDateTime())
                .user(user)
                .build()).update(team, matchTeamSide)).getId();
    }



    @Transactional
    public Long acceptRequest(Long joinGameRequestId) {
        JoinGameRequest joinGameRequest = joinGameRequestRepository.findById(joinGameRequestId).orElseThrow();
        if (joinGameRequest.isSoloTeam()) {
            return acceptSoloRequest(joinGameRequest);
        }
        return acceptTeamRequest(joinGameRequest);
    }

    private Long acceptSoloRequest(JoinGameRequest joinGameRequest) {
        SubTeam soloTeam = teamSelector.getSoloSubTeam(joinGameRequest.getGame(), joinGameRequest.getMatchTeamSide());
        return addUserToTeam(soloTeam, joinGameRequest.getUser());
    }

    private Long getSubTeam(JoinGameRequest joinGameRequest) {
        CompetingTeam competingTeam = joinGameRequest.getGame().getCompetingTeamBySide(joinGameRequest.getMatchTeamSide());
        return competingTeam.findSubTeam(joinGameRequest.getTeam()).orElse(subTeamRepository.save(SubTeam.builder()
                .competingTeam(competingTeam)
                .isAccept(true)
                .isSoloTeam(false)
                .team(joinGameRequest.getTeam())
                .build())).getId();
    }

    private Long acceptTeamRequest(JoinGameRequest joinGameRequest) {
        SubTeam subTeam = subTeamRepository.findById(getSubTeam(joinGameRequest)).orElseThrow();
        return addUserToTeam(subTeam, joinGameRequest.getUser());
    }


    @Transactional
    public void rejectRequest(Long joinGameRequestId) {
        JoinGameRequest joinGameRequest = joinGameRequestRepository.findById(joinGameRequestId).orElseThrow();
        joinGameRequestRepository.delete(joinGameRequest);
    }

    public Long addUserToTeam(SubTeam subTeam, User user) {
        return gameParticipantRepository.save(GameParticipant.builder()
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
