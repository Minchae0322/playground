package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.type.SportsEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class GameThumbnail {

    private LocalDateTime gameStart;

    private Long time;

    private SportsEvent sportsEvent;

    private String hostName;

    @Builder
    public GameThumbnail(LocalDateTime gameStart, Long time, SportsEvent sportsEvent, String hostName) {
        this.gameStart = gameStart;
        this.time = time;
        this.sportsEvent = sportsEvent;
        this.hostName = hostName;
    }
}
