package com.example.playgroundmanage.util;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.exception.UserNotParticipantGameException;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.game.vo.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GameParticipantFinder {
    public List<GameParticipant> getGameParticipants(Game game) {
        return game.getGameParticipants();
    }

    @Transactional
    public Optional<GameParticipant> getParticipantInGame(Game game, User user) {
        return Optional.ofNullable(game.getGameParticipants().stream()
                .filter(gameParticipant -> gameParticipant.getUser().equals(user))
                .findFirst()
                .orElse(null)); // Optional.empty() 대신 null을 사용
    }

    public List<GameParticipant> getGameOfSameYearAndMonth(List<GameParticipant> gameParticipants, LocalDateTime localDateTime) {
        int targetYear = localDateTime.getYear();
        int targetMonth = localDateTime.getMonthValue();

        return gameParticipants.stream()
                .filter(gameParticipant -> isSameYearAndMonth(gameParticipant, targetYear, targetMonth))
                .toList();
    }

    private boolean isSameYearAndMonth(GameParticipant gameParticipant, int targetYear, int targetMonth) {
        LocalDateTime gameStartDateTime = gameParticipant.getGame().getGameStartDateTime();
        return gameStartDateTime.getYear() == targetYear && gameStartDateTime.getMonthValue() == targetMonth;
    }
}
