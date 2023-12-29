package com.example.playgroundmanage.game.service.impl;

import com.example.playgroundmanage.dto.JoinGameRequestDto;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.game.vo.impl.SoloGameJoinRequest;
import com.example.playgroundmanage.game.vo.impl.TeamGameJoinRequest;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.util.TeamSelector;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public String getRequestType() {

        return "teamGameJoin";
    }


    @Transactional
    private Long saveJoinRequest(Game game, JoinGameRequestDto joinGameRequestDto) {

        //TeamGameJoinRequest teamGameJoinRequest = (TeamGameJoinRequest) gameJoinRequestRepository.findByGameAndUser(game, joinGameRequestDto.getUser()).orElseThrow().update(team);
        return null;
    }


}
