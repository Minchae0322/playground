package com.example.playgroundmanage.location.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PlaygroundRequestDto {

    private Long campusId;

    private String playgroundName;

    private MultipartFile playgroundProfileImg;

    private String sportsEvent;

    @Builder
    public PlaygroundRequestDto(Long campusId, String playgroundName, MultipartFile playgroundProfileImg, String sportsEvent) {
        this.campusId = campusId;
        this.playgroundName = playgroundName;
        this.playgroundProfileImg = playgroundProfileImg;
        this.sportsEvent = sportsEvent;
    }
}
