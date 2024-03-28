package com.example.playgroundmanage.login.dto;

import lombok.Builder;
import lombok.Data;

public class UserDto {

    @Data
    public static class UserRequestDto {
        private String username;

        private String password;


        @Builder
        public UserRequestDto(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
