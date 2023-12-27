package com.example.playgroundmanage.util;

import com.example.playgroundmanage.dto.GameTimeInfo;
import com.example.playgroundmanage.dto.response.GameTimeDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.vo.Playground;

import java.util.List;

public final class GameValidation {

    public static void validateGameStartTime(Playground playground, GameTimeDto gameTimeDto) {
        GameFinder gameFinder = new GameFinder(playground.getGames());

        List<Game> gamesOnSelectedDate = gameFinder.getGamesForSelectedDate(gameTimeDto.getMyDateTime());

        for (Game game : gamesOnSelectedDate) {
            if (game.isTimeRangeOverlapping(gameTimeDto.getMyDateTime().getLocalDateTime(), gameTimeDto.getRunningTime())) {
                throw new IllegalArgumentException("지정한 시간에 게임이 이미 존재합니다.");
            }
        }
    }
}
