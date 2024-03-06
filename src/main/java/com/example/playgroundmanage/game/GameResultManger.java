package com.example.playgroundmanage.game;

import com.example.playgroundmanage.game.dto.GameResultDto;

public interface GameResultManger {

    String getType();

    void submitGameResult(GameResultDto.GameResultRequestDto gameResultRequestDto);
}
