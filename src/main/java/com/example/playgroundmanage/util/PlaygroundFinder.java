package com.example.playgroundmanage.util;

import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.type.SportsEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PlaygroundFinder {

    public List<Playground> getPlaygroundsBySportsEvent(List<Playground> playgrounds, SportsEvent sportsEvent) {
        return playgrounds.stream()
                .filter(playground -> playground.getSportsEvent().equals(sportsEvent))
                .toList();
    }
}
