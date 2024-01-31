package com.example.playgroundmanage.type;

import com.example.playgroundmanage.game.vo.Game;

public enum GameType {
    COMPETITION("Competition", "竞争"),
    FRIENDLY("Friendly", "友谊赛");

    private final String value;
    private final String value2;

    GameType(String value, String value2) {
        this.value = value;
        this.value2 = value2;
    }

    public String getValue_cn() {
        return value2;
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
