package com.example.playgroundmanage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Getter
@RequiredArgsConstructor
public class GameTimeline {
    private String start;

    private String end;

    @Builder
    public GameTimeline(LocalDateTime start, LocalDateTime end) {
        this.start = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        this.end = end.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
