package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.JoinGameRequestDto;
import lombok.RequiredArgsConstructor;

public interface RequestService {

    Long generateRequest(Long gameId, JoinGameRequestDto joinGameRequestDto);

    String getRequestType();

    Long acceptRequest(Long requestId);

    void declineRequest(Long requestId);
}
