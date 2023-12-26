package com.example.playgroundmanage.util;

import com.example.playgroundmanage.dto.GameDateDto;
import com.example.playgroundmanage.game.vo.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class GameFinder {
    private final List<Game> games;

    public List<Game> getGamesForSelectedDate(GameDateDto gameDateDto) {
        return games.stream().filter(g -> g.isGameDay(gameDateDto.toLocalDateYYMMDD()))
                .toList();
    }
}
