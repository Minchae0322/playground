package com.example.playgroundmanage.type;

public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER");


    UserRole(String role) {
        this.role = role;
    }

    public String getValue() {
        return this.role;
    }

    private final String role;
}
