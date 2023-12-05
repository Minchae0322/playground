package com.example.playgroundmanage.type;

public enum MatchTeamSide {
    HOME("HOME"),
    AWAY("AWAY"),
    NONE("NONE"),
    ;

    private final String value;
    MatchTeamSide(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
