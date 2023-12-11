package com.example.playgroundmanage.dto;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class GameDateDto {

    private Long gameId;
    private String year;
    private String month;
    private String date;

    @Builder
    public GameDateDto(Long gameId, String year, String month, String date) {
        this.gameId = gameId;
        this.year = year;
        this.month = month;
        this.date = date;
    }
}
