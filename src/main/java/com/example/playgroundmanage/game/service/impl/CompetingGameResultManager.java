package com.example.playgroundmanage.game.service.impl;


import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.game.GameResultInjection;
import com.example.playgroundmanage.game.GameResultManger;
import com.example.playgroundmanage.game.dto.GameResultDto;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.GameResultRepository;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameResult;
import com.example.playgroundmanage.type.GameType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompetingGameResultManager implements GameResultManger {

    private final GameRepository gameRepository;

    private final GameResultRepository gameResultRepository;

    private final GameResultInjection gameResultInjection;

    @Override
    public String getType() {
        return "Competition";
    }

    @Override
    @Transactional
    public void submitGameResult(GameResultDto.GameResultRequestDto gameResultRequestDto) {
        Game game = gameRepository.findById(gameResultRequestDto.getGameId())
                .orElseThrow(GameNotExistException::new);

        if (game.getGameType() != GameType.COMPETITION) {
            throw new RuntimeException();
        }

        GameResult gameResult = gameResultRequestDto.toGameResult(game);
        saveGameResult(game, gameResult);

        gameResultInjection.injectResult(game, gameResult);
    }

    @Override
    public GameResultDto.GameResultResponseDto getGameResult(Long gameId) {
        return null;
    }

    @Transactional
    public void saveGameResult(Game game, GameResult gameResult) {
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
