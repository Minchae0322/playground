package com.example.playgroundmanage.type;

import com.example.playgroundmanage.game.vo.Game;

public enum GameType {
    COMPETITION("Competition"),
    FRIENDLY("Friendly");

    private final String value;

    GameType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static GameType fromString(String value) {
        for (GameType event : GameType.values()) {
            if (event.getValue().equalsIgnoreCase(value)) {
                return event;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found");
        // 또는 null 반환을 원하면 return null; 사용
    }
}
