package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.game.repository.GameRequestRepository;
import com.example.playgroundmanage.game.repository.GameParticipantRepository;
import com.example.playgroundmanage.game.repository.TeamRequestRepository;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.request.vo.GameRequest;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class GameManagementService {

    private final GameRequestRepository gameRequestRepository;

    private final TeamRequestRepository teamRequestRepository;
    private final GameParticipantRepository gameParticipantRepository;

    public void deletePreviousGameRequest(Game game, User user) {
        gameRequestRepository.findByGameAndUser(game, user)
                .ifPresent(gameRequestRepository::delete);
    }

    public void deletePreviousTeamRequest(Team team, User user) {
        teamRequestRepository.findByTeamAndUser(team, user)
                .ifPresent(teamRequestRepository::delete);
    }

    @Transactional
    public List<GameParticipant> findGameParticipantsInGame(Game game) {
        return game.getGameParticipants();
    }

    public void deleteRequest(Long requestId) {
        gameRequestRepository.findById(requestId)
                .ifPresent(gameRequestRepository::delete);
    }


   /* @Transactional
    public List<RequestInfoDto> getPendingGameRequests(User host) {
        List<GameRequest> pendingRequests = gameRequestRepository.findAllByHostAndExpiredTimeAfter(host, LocalDateTime.now());

        return pendingRequests.stream()
                .map(GameRequest::toGameRequestInfoDto)
                .toList();
    }

    @Transactional
    public List<RequestInfoDto> getPendingTeamGameJoinRequests(User user) {
        List<GameRequest> pendingRequests = gameRequestRepository.findAllByHostAndExpiredTimeAfter(user, LocalDateTime.now());

        return pendingRequests.stream()
                .map(GameRequest::toGameRequestInfoDto)
                .toList();
    }*/
}
