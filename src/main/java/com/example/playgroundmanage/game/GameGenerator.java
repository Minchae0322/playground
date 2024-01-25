package com.example.playgroundmanage.game;

import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.location.vo.Playground;

public interface GameGenerator {

    String getType();

    Long generate(GameDto gameDto);

}
