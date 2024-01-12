package com.example.playgroundmanage.util;

import com.example.playgroundmanage.exception.UserNotParticipantGameException;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.game.vo.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

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

}
