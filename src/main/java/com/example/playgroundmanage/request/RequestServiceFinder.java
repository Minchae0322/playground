package com.example.playgroundmanage.request;

import com.example.playgroundmanage.request.service.AthleticsRequestService;
import com.example.playgroundmanage.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestServiceFinder {

    private final List<AthleticsRequestService> requestServices;

    public AthleticsRequestService find(String type) {
        return requestServices.stream()
                .filter(requestService -> requestService.getRequestType().equals(type))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
