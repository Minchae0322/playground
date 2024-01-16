package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.date.DateTime;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public class UsersGameDto {

    @Data
    public static class UsersGameRequestDto {
        private User user;
        private DateTime myDateTime;

        @Builder
        public UsersGameRequestDto(User user, DateTime myDateTime) {
            this.user = user;
            this.myDateTime = myDateTime;
        }
    }

    @Data
    public static class UsersGameResponseDto implements Comparable<UsersGameResponseDto> {

        private String gameName;
        private String hostName;
        private String gameStart;
        private Integer runningTime;
        private LocalDateTime localDateStartTime;


        @Builder
        public UsersGameResponseDto(String gameName, String hostName, String gameStart, Integer runningTime, LocalDateTime localDateTime) {
            this.gameName = gameName;
            this.hostName = hostName;
            this.gameStart = gameStart;
            this.runningTime = runningTime;
            this.localDateStartTime = localDateTime;
        }




        @Override
        public int compareTo(UsersGameResponseDto usersGameResponseDto) {
            return 0;
        }
    }

}
