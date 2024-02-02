package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.date.DateTime;
import com.example.playgroundmanage.game.vo.User;
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

        private String location;

        private String hostName;

        private String gameStart;

        private Integer runningTime;

        private Long playgroundId;

        private LocalDateTime localDateStartTime;


        @Builder
        public UsersGameResponseDto(Long gameId, String subTeamName, Long playgroundId, GameType gameType, String location, String gameName, String hostName, String gameStart, Integer runningTime, LocalDateTime localDateStartTime) {
            this.gameId = gameId;
            this.subTeamName = subTeamName;
            this.gameName = gameName;
            this.hostName = hostName;
            this.gameStart = gameStart;
            this.playgroundId = playgroundId;
            this.runningTime = runningTime;
            this.location = location;
            this.localDateStartTime = localDateStartTime;
        }






        @Override
        public int compareTo(UsersGameResponseDto usersGameResponseDto) {
            return 0;
        }
    }

}
