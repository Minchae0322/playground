package com.example.playgroundmanage.game;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GameResultManagerFactory {

    private final List<GameResultManger> gameResultMangers;

    public GameResultManger find(String type) {
        return gameResultMangers.stream()
                .filter(requestService -> requestService.getType().equals(type))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
