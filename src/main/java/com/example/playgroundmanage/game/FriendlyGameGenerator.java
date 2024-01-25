package com.example.playgroundmanage.game;

import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.game.dto.GameTeamResponseDto;
import com.example.playgroundmanage.type.GameTeamSide;
import org.springframework.stereotype.Component;

@Component
public class FriendlyGameGenerator implements GameGenerator{


    @Override
    public String getType() {
        return "Friendly";
    }

    @Override
    public Long generate(GameDto gameDto) {
        return null;
    }

    @Override
    public GameTeamResponseDto getGameTeamInfos(Long gameId, GameTeamSide gameTeamSide) {
        return null;
    }
}
