package com.example.playgroundmanage.location.dto;

import com.example.playgroundmanage.location.vo.Playground;
import lombok.Builder;
import lombok.Data;
import lombok.Value;


@Data
public class PlaygroundResponseDto {
    private Long playgroundId;

    private String playgroundName;

    private String sportsEvent;

    private String playgroundProfileImg;

    private String schoolName;

    private String campusName;

    private Integer upcomingGameNum;


    @Builder
    public PlaygroundResponseDto(Long playgroundId, Integer upcomingGameNum, String playgroundName, String sportsEvent, String playgroundProfileImg, String schoolName, String campusName) {
        this.playgroundId = playgroundId;
        this.playgroundName = playgroundName;
        this.sportsEvent = sportsEvent;
        this.playgroundProfileImg = playgroundProfileImg;
        this.schoolName = schoolName;
        this.campusName = campusName;
        this.upcomingGameNum = upcomingGameNum;
    }


}
