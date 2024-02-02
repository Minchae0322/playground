package com.example.playgroundmanage.util;

import com.example.playgroundmanage.game.vo.Game;
import org.springframework.stereotype.Component;

@Component
public class LocationFormatter {

    public static String getLocation(Game game) {
        return game.getPlayground().getCampus().getCampusName() + ", " + game.getPlayground().getName();
    }
}
