package com.example.playgroundmanage.request.service;

import com.example.playgroundmanage.request.dto.AthleticsJoinRequest;
import com.example.playgroundmanage.request.dto.PendingRequestResponse;

import java.util.List;

public interface AthleticsRequestService {

    Long generateRequest(Long userId, AthleticsJoinRequest athleticsJoinRequest);

    String getRequestType();

    Long acceptRequest(Long requestId);

    List<PendingRequestResponse> getPendingRequests(Long hostId);

    void rejectRequest(Long requestId);
}
