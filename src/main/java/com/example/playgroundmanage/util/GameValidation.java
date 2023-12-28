package com.example.playgroundmanage.util;

import com.example.playgroundmanage.dto.response.GameTimeDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.vo.Playground;

import java.util.Collections;
import java.util.Optional;

public final class GameValidation {

    public static void validateOverlappingGames(Playground playground, GameTimeDto gameTimeDto) {
        Optional.ofNullable(playground.getGames())
                .orElse(Collections.emptyList()) // 빈 리스트 반환으로 NullPointException 방지
                .stream()
                .filter(game -> GameFinder.isGameOnSameDate(game, gameTimeDto.getStartDateTime())) // 같은 날짜의 게임만 필터링
                .forEach(game -> validateGameNotOverlapping(game, gameTimeDto));
    }

    private static void validateGameNotOverlapping(Game game, GameTimeDto gameTimeDto) {
        if (game.isTimeRangeOverlapping(gameTimeDto.getStartDateTime().getLocalDateTime(), gameTimeDto.getRunningTime())) {
            throw new IllegalArgumentException("지정한 시간에 게임이 이미 존재합니다.");
        }
    }
}
