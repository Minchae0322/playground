package com.example.playgroundmanage.login.dto;

import com.example.playgroundmanage.date.DateTime;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameType;
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

        private Long gameId;

        private String subTeamName;

        private String gameName;

        private String gameType;

        private String gameType_en;

        private String location;

        private String hostName;

        private String gameStart;

        private Integer runningTime;

        private Long playgroundId;

        private LocalDateTime localDateStartTime;

        private boolean isResulted;

        @Builder
        public UsersGameResponseDto(Long gameId, String subTeamName, String gameName, String gameType, String gameType_en, String location, String hostName, String gameStart, Integer runningTime, Long playgroundId, LocalDateTime localDateStartTime, boolean isResulted) {
            this.gameId = gameId;
            this.subTeamName = subTeamName;
            this.gameName = gameName;
            this.gameType = gameType;
            this.gameType_en = gameType_en;
            this.location = location;
            this.hostName = hostName;
            this.gameStart = gameStart;
            this.runningTime = runningTime;
            this.playgroundId = playgroundId;
            this.localDateStartTime = localDateStartTime;
            this.isResulted = isResulted;
        }








        @Override
        public int compareTo(UsersGameResponseDto usersGameResponseDto) {
            return 0;
        }
    }

}
