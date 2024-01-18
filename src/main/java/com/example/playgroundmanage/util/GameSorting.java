package com.example.playgroundmanage.util;

import com.example.playgroundmanage.game.vo.Game;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class GameSorting {

    public List<Game> sortGamesByLatest(List<Game> games) {
        return games.stream()
                .sorted(Comparator.comparing(Game::getGameStartDateTime))
                .toList();
    }

}
