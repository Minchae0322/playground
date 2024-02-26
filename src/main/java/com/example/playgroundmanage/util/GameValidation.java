package com.example.playgroundmanage.util;

import com.example.playgroundmanage.dto.GameTimeDto;
import com.example.playgroundmanage.exception.TimeOverlappingException;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.game.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.playgroundmanage.util.Util.localDateToYearMonthDateTimeString;


@RequiredArgsConstructor
@Component
public final class GameValidation {
    private final GameFinder gameFinder;

    public void validateOverlappingGames(List<Game> games, GameTimeDto gameTimeDto) {
        boolean isOverlapping = games.stream()
                .anyMatch(game -> gameFinder.isDateTimeRangeOverlapping(game, gameTimeDto.getStartDateTime().getLocalDateTime(), gameTimeDto.getRunningTime()));

        if (isOverlapping) {
            throw new TimeOverlappingException(localDateToYearMonthDateTimeString(LocalDateTime.now()));
        }
    }

    public static void validateStartBeforePresent(GameTimeDto gameTimeDto) {
        if (gameTimeDto.getStartDateTime().getLocalDateTime().isBefore(LocalDateTime.now().plusMinutes(1))) {
            throw new RuntimeException("您不能提前于当前时间 + 1 分钟开始 。 当前时间 : " + localDateToYearMonthDateTimeString(LocalDateTime.now()));
        }
    }

    public static void validateDuplicateUserInGame(List<GameParticipant> gameParticipants, User user) {
        boolean isAlreadyParticipant = gameParticipants.stream()
                .anyMatch(gp -> gp.getUser().equals(user));

        if (isAlreadyParticipant) {
            throw new IllegalArgumentException("已经参与比赛的用户: ");
        }
    }


}
