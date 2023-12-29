package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.JoinGameRequestDto;
import org.springframework.stereotype.Service;

public interface RequestService {
    Long generateJoinRequest(Long gameId, JoinGameRequestDto joinGameRequestDto);

    String getRequestType();

}
