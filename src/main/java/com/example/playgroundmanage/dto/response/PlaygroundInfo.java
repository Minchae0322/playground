package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.store.impl.FileHandlerImpl;
import com.example.playgroundmanage.type.SportsEvent;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PlaygroundInfo {
    private Long playgroundId;


    private String playgroundProfileImg;

    private String schoolName;

    private String campusName;

    private String playgroundName;

    private String sportsEvent;

    @Builder
    public PlaygroundInfo(Long playgroundId, InMemoryMultipartFile playgroundProfileImg, String schoolName, String campusName, String playgroundName, SportsEvent sportsEvent) {
        this.playgroundId = playgroundId;
        this.playgroundProfileImg = FileHandlerImpl.multipartFileToString(playgroundProfileImg);
        this.schoolName = schoolName;
        this.campusName = campusName;
        this.playgroundName = playgroundName;
        this.sportsEvent = sportsEvent.getValue();
    }
}
