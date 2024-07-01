package com.example.playgroundmanage.game.dto;


import lombok.Builder;
import lombok.Data;

public class GameResultDto {

    private int gameId;

    @Data
    public static class GameResultRequestDto {

        private Long gameId;

        private int homeScore;

        private int awayScore;

        @Builder
        public GameResultRequestDto(Long gameId, int homeScore, int awayScore) {
            this.gameId = gameId;
            this.homeScore = homeScore;
            this.awayScore = awayScore;
        }


    }

    @Data
    public static class GameResultResponseDto {

        private Long gameId;

        private int homeScore;

        private int awayScore;

        @Builder
        public GameResultResponseDto(Long gameId, int homeScore, int awayScore) {
            this.gameId = gameId;
            this.homeScore = homeScore;
            this.awayScore = awayScore;
        }



    }
}
