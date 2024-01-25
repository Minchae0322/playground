package com.example.playgroundmanage.game;

import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.game.dto.GameTeamResponseDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.type.GameTeamSide;

import java.util.List;

public interface GameGenerator {

    String getType();

    Long generate(GameDto gameDto);

    GameTeamResponseDto getGameTeamInfos(Long gameId, GameTeamSide gameTeamSide);

}
