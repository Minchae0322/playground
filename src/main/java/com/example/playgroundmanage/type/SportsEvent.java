package com.example.playgroundmanage.type;

public enum SportsEvent {
    BASKETBALL("Basketball"),
    SOCCER("Soccer"),
    BADMINTON("Badminton"),

    TENNIS("Tennis");

    private final String value;

    SportsEvent(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SportsEvent fromString(String value) {
        for (SportsEvent event : SportsEvent.values()) {
            if (event.getValue().equalsIgnoreCase(value)) {
                return event;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found");
        // 또는 null 반환을 원하면 return null; 사용
    }
}
