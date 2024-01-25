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

    public String getValue() {
        return this.value;
    }
}
