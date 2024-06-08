package com.example.playgroundmanage.request.service;

import com.example.playgroundmanage.request.dto.AthleticsJoinRequest;

public interface AthleticsRequestService {

    Long generateRequest(Long userId, AthleticsJoinRequest athleticsJoinRequest);

    String getRequestType();

    Long acceptRequest(Long requestId);
}
