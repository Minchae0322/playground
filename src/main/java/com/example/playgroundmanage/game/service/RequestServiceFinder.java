package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.game.vo.JoinGameRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestServiceFinder {

    private final List<RequestService> requestServices;

    public RequestService find(String type) {
        return requestServices.stream()
                .filter(requestService -> requestService.getRequestType().equals(type))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
