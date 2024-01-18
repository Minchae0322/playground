package com.example.playgroundmanage.util;

import com.example.playgroundmanage.date.DateTime;
import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.exception.NoOngoingGameException;
import com.example.playgroundmanage.game.vo.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Getter
@Component
@AllArgsConstructor
public class GameFinder {

    public static List<Game> getGamesForSelectedDate(List<Game> games, DateTime myDateTime) {
        return Optional.ofNullable(games)
                .orElse(Collections.emptyList()) // 빈 리스트 반환으로 NullPointException 방지
                .stream()
                .filter(g -> g.isGameDay(myDateTime.getLocalDateTime()))
                .toList();
    }

    public static boolean isGameOnSameDate(Game game, DateTime myDateTime) {
        return game.isGameDay(myDateTime.getLocalDateTime());
    }
    public static Game findOngoingGame(List<Game> games, MyDateTime now) {
        return games.stream()
                .filter(game -> game.isGameOngoing(now.getLocalDateTime()))
                .findFirst()
                .orElseThrow(NoOngoingGameException::new);
    }

    public List<Game> getUpcomingGames(List<Game> games, Integer numberOfGame, DateTime myDateTime) {
        return Optional.ofNullable(games)
                .orElse(Collections.emptyList()) // 빈 리스트 반환으로 NullPointException 방지
                .stream()
                .filter(game -> game.getGameStartDateTime().isAfter(myDateTime.getLocalDateTime()))
                .sorted(Comparator.comparing(Game::getGameStartDateTime))
                .limit(numberOfGame)
                .toList();
    }
}
