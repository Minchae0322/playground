package com.example.playgroundmanage.game.service.impl;

import com.example.playgroundmanage.dto.JoinGameRequestDto;
import com.example.playgroundmanage.game.service.RequestService;
import org.springframework.stereotype.Service;

@Service
public class TeamGameJoinRequestService implements RequestService {
    @Override
    public Long generateJoinRequest(Long gameId, JoinGameRequestDto joinGameRequestDto) {
        return null;
    }

    @Override
    public String getRequestType() {
        return "teamGameJoin";
    }


}
