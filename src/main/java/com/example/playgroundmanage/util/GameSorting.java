package com.example.playgroundmanage.util;

import com.example.playgroundmanage.game.vo.Game;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class GameSorting {

    public List<Game> sortGamesByEarliest(List<Game> games) {
        return games.stream()
                .sorted(Comparator.comparing(Game::getGameStartDateTime))
                .toList();
    }

    public List<Game> sortGamesByOldest(List<Game> games) {
        return games.stream()
                .sorted(Comparator.comparing(Game::getGameStartDateTime).reversed())
                .toList();
    }

}
