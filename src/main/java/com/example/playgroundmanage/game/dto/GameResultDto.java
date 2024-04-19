package com.example.playgroundmanage.game.dto;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameResult;
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

        public GameResult toGameResult(Game game) {
            return GameResult.builder()
                    .game(game)
                    .homeScore(homeScore)
                    .awayScore(awayScore)
                    .build();
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

        public GameResult toGameResult(Game game) {
            return GameResult.builder()
                    .game(game)
                    .homeScore(homeScore)
                    .awayScore(awayScore)
                    .build();
        }

    }
}
