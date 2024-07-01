package com.example.playgroundmanage.althlectis.dto.request;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.AthleticsResult;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record GameResultRequest(

        @NotBlank(message = "game ID must not be null.")
        Long gameId,

        @NotBlank(message = "home score must not be null.")
        @Min(value = 0, message = "at Least 1")
        int homeScore,

        @NotBlank(message = "away score must not be null.")
        @Min(value = 0, message = "at Least 1")
        int awayScore
) {

        public static AthleticsResult from(Athletics athletics, GameResultRequest gameResultRequest) {
                return AthleticsResult.builder()
                        .athletics(athletics)
                        .homeScore(gameResultRequest.homeScore)
                        .awayScore(gameResultRequest.awayScore)
                        .build();
        }

}
