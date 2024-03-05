package com.example.playgroundmanage.game;

import com.example.playgroundmanage.game.dto.GameResultDto;

public interface GameResultManger {

    void submitGameResult(GameResultDto.GameResultRequestDto gameResultRequestDto);
}
