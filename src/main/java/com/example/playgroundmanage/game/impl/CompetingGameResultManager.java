package com.example.playgroundmanage.game.impl;

import com.example.playgroundmanage.dto.GameTimeDto;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.game.GameResultInjection;
import com.example.playgroundmanage.game.GameResultManger;
import com.example.playgroundmanage.game.dto.GameResultDto;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.GameRequestRepository;
import com.example.playgroundmanage.game.repository.GameResultRepository;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CompetingGameResultManager implements GameResultManger {

    private final GameRepository gameRepository;

    private final GameResultRepository gameResultRepository;

    private final GameResultInjection gameResultInjection;

    @Override
    @Transactional
    public void submitGameResult(GameResultDto.GameResultRequestDto gameResultRequestDto) {
        Game game = gameRepository.findById(gameResultRequestDto.getGameId()).orElseThrow(GameNotExistException::new);
        GameResult gameResult = gameResultRequestDto.toGameResult(game);

        saveGameResult(gameResult);
        gameResultInjection.injectResult();
        //todo GameResultInjection 을 만들어서 참여했던 사람들 GameParticipant 에 결과 주입

    }

    private void saveGameResult(GameResult gameResult) {
        gameResultRepository.save(gameResult);
    }
}
