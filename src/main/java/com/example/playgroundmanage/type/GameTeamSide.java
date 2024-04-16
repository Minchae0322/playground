package com.example.playgroundmanage.type;

public enum GameTeamSide {

    HOME("HOME"),
    AWAY("AWAY"),
    NONE("NONE"),
    ;

    private final String value;
    GameTeamSide(String value) {
        this.value = value;
    }

    public static GameTeamSide fromString(String value) {
        for (GameTeamSide event : GameTeamSide.values()) {
            if (event.getValue().equalsIgnoreCase(value)) {
                return event;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found");
        // 또는 null 반환을 원하면 return null; 사용
    }

    public String getValue() {
        return this.value;
    }
}
