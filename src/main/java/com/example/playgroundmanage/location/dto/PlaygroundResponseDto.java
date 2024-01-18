package com.example.playgroundmanage.location.dto;

import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.store.impl.FileHandlerImpl;
import lombok.Builder;
import lombok.Data;

import static com.example.playgroundmanage.store.impl.FileHandlerImpl.multipartFileToString;

@Data
public class PlaygroundResponseDto {

    private Long playgroundId;

    private String playgroundName;

    private String sportsEvent;

    private String playgroundProfileImg;

    private String schoolName;

    private String campusName;

    @Builder
    public PlaygroundResponseDto(Long playgroundId, String playgroundName, String sportsEvent, InMemoryMultipartFile playgroundProfileImg, String schoolName, String campusName) {
        this.playgroundId = playgroundId;
        this.playgroundName = playgroundName;
        this.sportsEvent = sportsEvent;
        this.playgroundProfileImg = multipartFileToString(playgroundProfileImg);
        this.schoolName = schoolName;
        this.campusName = campusName;
    }
}
