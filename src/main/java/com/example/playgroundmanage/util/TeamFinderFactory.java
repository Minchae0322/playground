package com.example.playgroundmanage.util;

import com.example.playgroundmanage.game.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TeamFinderFactory {
    private final List<TeamFinder> requestServices;

    public TeamFinder find(String type) {
        return requestServices.stream()
                .filter(requestService -> requestService.getType().equals(type))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
