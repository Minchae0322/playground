package com.example.playgroundmanage.dto.response;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.store.impl.FileHandlerImpl;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.util.Util;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.Base64;

import static com.example.playgroundmanage.store.impl.FileHandlerImpl.multipartFileToString;

@Data
@RequiredArgsConstructor
public class GameThumbnail {

    private Long gameId;

    private String gameName;

    private String gameStart;

    private Integer runningTime;

    private SportsEvent sportsEvent;

    private String hostProfileImg;

    private String hostName;

    @Builder
    public GameThumbnail(Long gameId, String gameName, String gameStart, Integer runningTime, SportsEvent sportsEvent, InMemoryMultipartFile hostProfileImg, String hostName) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameStart = gameStart;
        this.runningTime = runningTime;
        this.sportsEvent = sportsEvent;
        this.hostProfileImg = multipartFileToString(hostProfileImg);
        this.hostName = hostName;
    }
}
