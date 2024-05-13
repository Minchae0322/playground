package com.example.playgroundmanage.game;

import com.example.playgroundmanage.game.service.GameGenerator;
import com.example.playgroundmanage.refactoring.service.AthleticsGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GameGeneratorFactory {

    private final List<GameGenerator> gameGenerators;

    public GameGenerator find(String type) {
        return gameGenerators.stream()
                .filter(requestService -> requestService.getType().equals(type))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
