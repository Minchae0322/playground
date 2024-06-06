package com.example.playgroundmanage.type;

public enum RequestState {

    PENDING("Pending"),
    REJECTED("Rejected"),
    ACCEPTED("Accepted"),
    ;

    private final String value;
    RequestState(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
