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

    private final DateFormat dateFormat;

    public List<Game> getGamesForSelectedDate(List<Game> games, DateTime myDateTime) {
        return Optional.ofNullable(games)
                .orElse(Collections.emptyList()) // 빈 리스트 반환으로 NullPointException 방지
                .stream()
                .filter(g -> isGameOnDate(g, myDateTime.getLocalDateTime()))
                .toList();
    }

    public List<Game> getGamesForYearMonth(List<Game> games, LocalDateTime localDateTime) {
        return Optional.ofNullable(games)
                .orElse(Collections.emptyList()) // 빈 리스트 반환으로 NullPointException 방지
                .stream()
                .filter(g -> isGameOnYearMonth(g, localDateTime))
                .toList();
    }

    private boolean isGameOnYearMonth(Game game, LocalDateTime localDateTime) {
        int targetYear = localDateTime.getYear();
        int targetMonth = localDateTime.getMonthValue();

        return game.getGameStartDateTime().getYear() == targetYear && game.getGameStartDateTime().getMonthValue() == targetMonth;
    }

    public boolean isGameOnDate(Game game, LocalDateTime targetDateTime) {
        LocalDateTime gameStartDateTime = game.getGameStartDateTime();

        return dateFormat.dateFormatYYYYMMDD(gameStartDateTime).equals(dateFormat.dateFormatYYYYMMDD(targetDateTime));
    }

    public boolean isDateTimeRangeOverlapping(Game game, LocalDateTime localDateTime, Integer gameRunningTime) {
        LocalDateTime startDateTime = dateFormat.dateFormatWith0Second(localDateTime);
        LocalDateTime endDateTime = dateFormat.dateFormatWith0Second(localDateTime).plusMinutes(gameRunningTime);

        LocalDateTime gameStartDateTime = game.getGameStartDateTime();
        LocalDateTime gameEndDateTime = gameStartDateTime.plusMinutes(game.getRunningTime());

        return !(gameEndDateTime.isBefore(startDateTime) || gameStartDateTime.isAfter(endDateTime));
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
