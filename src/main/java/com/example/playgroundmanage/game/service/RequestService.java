package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.dto.RequestDto;
import com.example.playgroundmanage.dto.reqeust.PendingRequestParams;

import java.util.List;

public interface RequestService {

    Long generateRequest(RequestDto RequestDto);

    String getRequestType();

    List<RequestInfoDto> getPendingRequests(PendingRequestParams pendingRequestParams);

    Long acceptRequest(Long requestId);

    void declineRequest(Long requestId);
}
