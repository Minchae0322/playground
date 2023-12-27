package com.example.playgroundmanage.util;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.game.vo.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GameFinder {
    private final List<Game> games;

    public List<Game> getGamesForSelectedDate(MyDateTime myDateTime) {
        return games.stream().filter(g -> g.isGameDay(myDateTime.getLocalDateTime()))
                .toList();
    }
}
