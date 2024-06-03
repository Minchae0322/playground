package com.example.playgroundmanage.location.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public record OccupiedTimeResponse(
        String start,
        String end
) {

    public static OccupiedTimeResponse of(LocalDateTime start, LocalDateTime end) {
        return new OccupiedTimeResponse(
                start.format(DateTimeFormatter.ofPattern("HH:mm")),
                end.format(DateTimeFormatter.ofPattern("HH:mm"))
        );
    }
}
