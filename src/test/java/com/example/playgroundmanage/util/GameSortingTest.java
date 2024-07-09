/*
package com.example.playgroundmanage.util;

import com.example.playgroundmanage.type.GameType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameSortingTest {

    @Autowired
    private GameSorting gameSorting;

    @Test
    void sortGamesByOldest() {
        //given
        Game game1 = Game.builder()
                .gameName("1")
                .runningTime(10)
                .gameType(GameType.FRIENDLY)
                .gameStartDateTime(LocalDateTime.of(2025, 1, 1, 1, 1))
                .build();

        Game game2 = Game.builder()
                .gameName("2")
                .runningTime(10)
                .gameType(GameType.FRIENDLY)
                .gameStartDateTime(LocalDateTime.of(2025, 1, 3, 7, 33))
                .build();

        Game game3 = Game.builder()
                .gameName("3")
                .runningTime(10)
                .gameType(GameType.FRIENDLY)
                .gameStartDateTime(LocalDateTime.of(2025, 1, 1, 2, 1))
                .build();

        Game game4 = Game.builder()
                .gameName("4")
                .runningTime(10)
                .gameType(GameType.FRIENDLY)
                .gameStartDateTime(LocalDateTime.of(2025, 1, 2, 2, 11))
                .build();

        List<Game> games = List.of(game1, game2, game3, game4);
        //when
        List<Game> result = gameSorting.sortGamesByOldest(games);

        //then
        assertEquals(LocalDateTime.of(2025, 1, 3, 7, 33), result.get(0).getGameStartDateTime());
        assertEquals(LocalDateTime.of(2025, 1, 2, 2, 11), result.get(1).getGameStartDateTime());
    }

    @Test
    void sortGamesByEarliest() {
        //given
        Game game1 = Game.builder()
                .gameName("1")
                .runningTime(10)
                .gameType(GameType.FRIENDLY)
                .gameStartDateTime(LocalDateTime.of(2025, 1, 1, 1, 1))
                .build();

        Game game2 = Game.builder()
                .gameName("2")
                .runningTime(10)
                .gameType(GameType.FRIENDLY)
                .gameStartDateTime(LocalDateTime.of(2025, 1, 3, 7, 33))
                .build();

        Game game3 = Game.builder()
                .gameName("3")
                .runningTime(10)
                .gameType(GameType.FRIENDLY)
                .gameStartDateTime(LocalDateTime.of(2025, 1, 1, 2, 1))
                .build();

        Game game4 = Game.builder()
                .gameName("4")
                .runningTime(10)
                .gameType(GameType.FRIENDLY)
                .gameStartDateTime(LocalDateTime.of(2025, 1, 2, 2, 11))
                .build();

        List<Game> games = List.of(game1, game2, game3, game4);
        //when
        List<Game> result = gameSorting.sortGamesByEarliest(games);

        //then
        assertEquals(LocalDateTime.of(2025, 1, 1, 1, 1), result.get(0).getGameStartDateTime());
        assertEquals(LocalDateTime.of(2025, 1, 1, 2, 1), result.get(1).getGameStartDateTime());
    }
}*/
