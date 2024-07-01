package com.example.playgroundmanage.login.dto;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.util.DateFormat;

import java.time.LocalDateTime;

import static com.example.playgroundmanage.util.LocationFormatter.getLocation;

public record UserGameInfoResponse(
       Long gameId,

       String gameName,

       String gameType,

       String gameType_en,

       String location,

       String hostName,

       String gameStart,

       Integer runningTime,

       Long playgroundId,

       LocalDateTime localDateStartTime,

       boolean isResulted
) {

    public static UserGameInfoResponse of(Athletics athletics) {
        return new UserGameInfoResponse(
                athletics.getId(),
                athletics.getGameName(),
                athletics.getGameType().getValue_cn(),
                athletics.getGameType().getValue(),
                getLocation(athletics),
                athletics.getHost().getNickname(),
                DateFormat.dateFormatYYYYMMDDHHMM(athletics.getGameStartDateTime()),
                athletics.getRunningTime(),
                athletics.getPlayground().getId(),
                athletics.getGameStartDateTime(),
                athletics.getAthleticsResult().isFinished()
        );
    }

}
