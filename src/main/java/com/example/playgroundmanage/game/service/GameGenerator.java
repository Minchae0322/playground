package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.game.dto.GameDto;
import com.example.playgroundmanage.game.dto.GameTeamRequestDto;
import com.example.playgroundmanage.game.dto.GameTeamResponseDto;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameTeamSide;

public interface GameGenerator {

    String getType();

    Long generate(GameDto gameDto);

    GameTeamResponseDto getGameTeamInfos(GameTeamRequestDto gameTeamRequestDto);

}
