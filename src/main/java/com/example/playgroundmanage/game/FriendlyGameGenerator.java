package com.example.playgroundmanage.game;

import com.example.playgroundmanage.dto.GameDto;
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
}
