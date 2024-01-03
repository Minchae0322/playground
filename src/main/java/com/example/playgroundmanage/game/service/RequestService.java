package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.GameRequestDto;

public interface RequestService {

    Long generateRequest(Long gameId, GameRequestDto gameRequestDto);

    String getRequestType();

    Long acceptRequest(Long requestId);

    void declineRequest(Long requestId);
}
