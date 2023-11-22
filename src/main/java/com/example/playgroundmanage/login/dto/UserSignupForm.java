package com.example.playgroundmanage.login.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupForm {
    private String username;
    private String password;

    @Builder
    public UserSignupForm(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
