package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.type.SportsEvent;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Base64;

@Data
@RequiredArgsConstructor
public class PlaygroundInfo {
    private Long playgroundId;


    private String playgroundImg;

    private String schoolName;

    private String campusName;

    private String playgroundName;

    private String sportsEvent;

    @Builder
    public PlaygroundInfo(Long playgroundId, InMemoryMultipartFile playgroundImg, String schoolName, String campusName, String playgroundName, SportsEvent sportsEvent) {
        this.playgroundId = playgroundId;
        try {
            this.playgroundImg = Base64.getEncoder().encodeToString(playgroundImg.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("이미지 변환 실패", e);
        }
        this.schoolName = schoolName;
        this.campusName = campusName;
        this.playgroundName = playgroundName;
        this.sportsEvent = sportsEvent.getValue();
    }
}
