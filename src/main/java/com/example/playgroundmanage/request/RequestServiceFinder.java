package com.example.playgroundmanage.request;

import com.example.playgroundmanage.request.service.AthleticsRequestService;
import com.example.playgroundmanage.request.service.RequestService;
import com.example.playgroundmanage.request.service.TeamRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RequestServiceFinder {

    private final Map<String, List<? extends RequestService>> services;

    @Autowired
    public RequestServiceFinder(List<AthleticsRequestService> athleticsRequestServices,
                                List<TeamRequestService> teamRequestServices) {
        this.services = new HashMap<>();
        this.services.put("athletics", athleticsRequestServices);
        this.services.put("team", teamRequestServices);
    }

    public RequestService find(String serviceType, String type) {
        return services.get(serviceType)
                .stream()
                .filter(requestService -> requestService.getRequestType().equals(type))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }


}
