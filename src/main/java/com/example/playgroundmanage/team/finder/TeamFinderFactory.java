package com.example.playgroundmanage.team.finder;

import com.example.playgroundmanage.team.finder.TeamFinder;
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
