package com.example.playgroundmanage.type;

public enum SportsEvent {
    BASKETBALL("Basketball","篮球"),
    SOCCER("Soccer","足球"),
    BADMINTON("Badminton","羽毛球"),

    TENNIS("Tennis","网球"),

    TABLE_TENNIS("Table_tennis","乒乓球");

    private final String value;
    private final String value_cn;

    SportsEvent(String value, String value_cn) {
        this.value = value;
        this.value_cn = value_cn;
    }

    public String getValue() {
        return value;
    }

    public String getValue_cn() {
        return value_cn;
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
