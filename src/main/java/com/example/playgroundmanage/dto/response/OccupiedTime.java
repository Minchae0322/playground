package com.example.playgroundmanage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Getter
@RequiredArgsConstructor
public class OccupiedTime {
    private String start;

    private String end;

    @Builder
    public OccupiedTime(LocalDateTime start, LocalDateTime end) {
        this.start = start.format(DateTimeFormatter.ofPattern("HH:mm"));
        this.end = end.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
