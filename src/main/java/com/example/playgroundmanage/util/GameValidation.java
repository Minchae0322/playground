package com.example.playgroundmanage.util;

import com.example.playgroundmanage.dto.GameTimeDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.game.vo.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.playgroundmanage.util.Util.localDateToYearMonthDateTimeString;


public final class GameValidation {

    public static void validateOverlappingGames(List<Game> games, GameTimeDto gameTimeDto) {
        boolean isOverlapping = games.stream()
                .anyMatch(game -> GameFinder.isGameOnSameDate(game, gameTimeDto.getStartDateTime())
                        && game.isTimeRangeOverlapping(gameTimeDto.getStartDateTime().getLocalDateTime(), gameTimeDto.getRunningTime()));

        if (isOverlapping) {
            throw new IllegalArgumentException("지정한 시간에 다른 게임이 이미 존재합니다: " + localDateToYearMonthDateTimeString(LocalDateTime.now()));
        }
    }

    public static void validateStartBeforePresent(GameTimeDto gameTimeDto) {
        if (gameTimeDto.getStartDateTime().getLocalDateTime().isBefore(LocalDateTime.now().plusMinutes(1))) {
            throw new IllegalArgumentException("현재시간 +1분 보다 전에 시작할 수 없습니다. 현재시간 : " + localDateToYearMonthDateTimeString(LocalDateTime.now()));
        }
    }

    public static void validateDuplicateUserInGame(List<GameParticipant> gameParticipants, User user) {
        boolean isAlreadyParticipant = gameParticipants.stream()
                .anyMatch(gp -> gp.getUser().equals(user));

        if (isAlreadyParticipant) {
            throw new IllegalArgumentException("이미 게임에 참여하고 있는 사용자입니다: ");
        }
    }


}
