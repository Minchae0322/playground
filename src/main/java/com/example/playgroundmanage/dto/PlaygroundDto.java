package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.SportsEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public class PlaygroundDto {

    private Long playgroundId;


    private String schoolName;

    private String campusName;

    private String playgroundName;

    private SportsEvent sportsEvent;

    @Builder
    public PlaygroundDto(Long playgroundId, String schoolName, String campusName, String playgroundName, SportsEvent sportsEvent) {
        this.playgroundId = playgroundId;
        this.schoolName = schoolName;
        this.campusName = campusName;
        this.playgroundName = playgroundName;
        this.sportsEvent = sportsEvent;
    }
}
