package com.example.playgroundmanage.game.impl;

import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.game.GameResultManger;
import com.example.playgroundmanage.game.dto.GameResultDto;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.GameResultRepository;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@RequiredArgsConstructor
public class FriendlyGameResultManager implements GameResultManger {

    private final GameRepository gameRepository;

    private final GameResultRepository gameResultRepository;

    @Override
    public String getType() {
        return "Friendly";
    }
    @Override
    public void submitGameResult(GameResultDto.GameResultRequestDto gameResultRequestDto) {
        Game game = gameRepository.findById(gameResultRequestDto.getGameId())
                .orElseThrow(GameNotExistException::new);

        GameResult gameResult = gameResultRequestDto.toGameResult(game);
        saveGameResult(game, gameResult);
    }

    @Transactional
    private void saveGameResult(Game game, GameResult gameResult) {
        gameResultRepository.findGameResultByGame(game)
                .ifPresentOrElse(existingResult -> {
                            existingResult.updateResult(gameResult);
                            gameResultRepository.save(existingResult);
                        },
                        () -> {
                            gameResultRepository.save(gameResult);
                        }
                );
    }
}
