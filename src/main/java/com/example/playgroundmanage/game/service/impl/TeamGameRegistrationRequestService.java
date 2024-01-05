package com.example.playgroundmanage.game.service.impl;

import com.example.playgroundmanage.dto.GameRequestDto;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.TeamNotExistException;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.GameManagementService;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.game.vo.impl.TeamGameRegistrationRequest;
import com.example.playgroundmanage.util.TeamSelector;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.playgroundmanage.util.GameValidation.validateDuplicateUserInGame;

@RequiredArgsConstructor
@Service
public class TeamGameRegistrationRequestService implements RequestService {

    private final GameRepository gameRepository;

    private final TeamRepository teamRepository;

    private final GameManagementService gameManagementService;

    private final TeamSelector teamSelector;

    private final SubTeamRepository subTeamRepository;

    private final GameParticipantRepository gameParticipantRepository;

    private final GameRequestRepository gameRequestRepository;


    @Override
    @Transactional
    public Long generateRequest(Long gameId, GameRequestDto gameRequestDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        validateDuplicateUserInGame(gameManagementService.findGameParticipantsInGame(game), gameRequestDto.getUser());
        gameManagementService.deletePreviousRequest(game, gameRequestDto.getUser());

        return saveJoinRequest(game, gameRequestDto);
    }


    @Transactional
    private Long saveJoinRequest(Game game, GameRequestDto gameRequestDto) {
        Team team = teamRepository.findById(gameRequestDto.getTeamId())
                .orElseThrow(TeamNotExistException::new);

        return gameRequestRepository.save(TeamGameRegistrationRequest.builder()
                        .game(game)
                        .team(team)
                        .host(game.getHost())
                        .requestTime(gameRequestDto.getRequestTime().getLocalDateTime())
                        .user(gameRequestDto.getUser())
                        .expiredTime(game.getGameStartDateTime().plusMinutes(game.getRunningTime()))
                        .matchTeamSide(gameRequestDto.getMatchTeamSide())
                        .build())
                .getId();
    }





    @Override
    public String getRequestType() {
        return "teamGameRegistration";
    }

    @Override
    @Transactional
    public Long acceptRequest(Long requestId) {
        TeamGameRegistrationRequest teamGameRegistrationRequest = (TeamGameRegistrationRequest) gameRequestRepository.findById(requestId)
                .orElseThrow();

        SubTeam subTeam = createSubTeam(teamGameRegistrationRequest.getTeam(), teamGameRegistrationRequest.getGame().getCompetingTeamBySide(teamGameRegistrationRequest.getMatchTeamSide()));

        gameManagementService.deleteRequest(teamGameRegistrationRequest.getId());

        return gameParticipantRepository.save(GameParticipant.builder()
                .isAccepted(true)
                .subTeam(subTeam)
                .user(teamGameRegistrationRequest.getUser())
                .build()).getId();
    }

    @Transactional
    private SubTeam createSubTeam(Team team, CompetingTeam competingTeam) {
        return subTeamRepository.save(SubTeam.builder()
                .isAccept(true)
                .isSoloTeam(false)
                .competingTeam(competingTeam)
                .team(team)
                .build());
    }

    @Override
    public void declineRequest(Long requestId) {
        gameManagementService.deleteRequest(requestId);
    }
}