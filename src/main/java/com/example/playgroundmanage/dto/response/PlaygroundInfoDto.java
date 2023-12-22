package com.example.playgroundmanage.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public class PlaygroundInfoDto {
    private String schoolName;

    private String campusName;

    private String playgroundName;

    private String sportsEvent;

    @Builder
    public PlaygroundInfoDto(String schoolName, String campusName, String playgroundName, String sportsEvent) {
        this.schoolName = schoolName;
        this.campusName = campusName;
        this.playgroundName = playgroundName;
        this.sportsEvent = sportsEvent;
    }
}
