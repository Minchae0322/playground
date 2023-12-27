package com.example.playgroundmanage.dto.reqeust;

import com.example.playgroundmanage.type.SportsEvent;
import lombok.Builder;
import lombok.Data;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.ZonedDateTime;

@Data
@RequiredArgsConstructor
public class GameRegistration {

    private Long playgroundId;

    private String gameName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private ZonedDateTime gameStartDateTime;

    private SportsEvent sportsEvent;

    private Integer runningTime;

    @Builder
    public GameRegistration(Long playgroundId, String gameName, ZonedDateTime gameStartDateTime, SportsEvent sportsEvent, Integer runningTime) {
        this.playgroundId = playgroundId;
        this.gameName = gameName;
        this.gameStartDateTime = gameStartDateTime;
        this.sportsEvent = sportsEvent;
        this.runningTime = runningTime;
    }


}
