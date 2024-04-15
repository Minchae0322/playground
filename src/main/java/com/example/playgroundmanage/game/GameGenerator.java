package com.example.playgroundmanage.game;

import com.example.playgroundmanage.game.dto.GameDto;
import com.example.playgroundmanage.game.dto.GameTeamResponseDto;
import com.example.playgroundmanage.type.GameTeamSide;

public interface GameGenerator {

    String getType();

    Long generate(GameDto gameDto);

    GameTeamResponseDto getGameTeamInfos(Long gameId, GameTeamSide gameTeamSide);

}
