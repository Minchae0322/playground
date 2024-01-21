package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.RequestDto;
import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.dto.reqeust.PendingRequestParams;
import com.example.playgroundmanage.request.service.RequestService;

import java.util.List;

public class GameRequestService implements RequestService {
//todo GameRequesteService 랑 TeamRequestService 를 나누고 GameRequestSerivce 에서 GameRequestProcessor 로 구현을 하는게 더 좋은 추상화 같다.
    @Override
    public Long generateRequest(RequestDto RequestDto) {
        return null;
    }

    @Override
    public String getRequestType() {
        return null;
    }

    @Override
    public List<RequestInfoDto> getPendingRequests(PendingRequestParams pendingRequestParams) {
        return null;
    }

    @Override
    public Long acceptRequest(Long requestId) {
        return null;
    }

    @Override
    public void declineRequest(Long requestId) {

    }
}
