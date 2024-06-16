package com.example.playgroundmanage.request.service;

import com.example.playgroundmanage.request.dto.PendingRequestResponse;
import com.example.playgroundmanage.request.dto.TeamJoinRequestDto;

import java.util.List;

public interface TeamRequestService {

    Long generateRequest(Long userId, TeamJoinRequestDto teamJoinRequestDto);

    String getRequestType();

    Long acceptRequest(Long requestId);

    List<PendingRequestResponse> getPendingRequests(Long hostId);

    void rejectRequest(Long requestId);


}
