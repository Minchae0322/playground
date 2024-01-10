package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.GameRequestDto;
import com.example.playgroundmanage.dto.RequestDto;

public interface RequestService {

    Long generateRequest(RequestDto RequestDto);

    String getRequestType();

    Long acceptRequest(Long requestId);

    void declineRequest(Long requestId);
}
