package com.example.playgroundmanage.location.dto;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.util.Util;

public record AthleticsThumbnailResponse(
        Long gameId,
        Long playgroundId,

        String hostName,

        String hostProfileImg,

        String gameType,

        String playgroundName,

        String gameName,

        String gameStartDateTime,

        String sportsEvent,

        Integer runningTime,

        String campusName
) {
    public static AthleticsThumbnailResponse of(Athletics athletics) {
        return new AthleticsThumbnailResponse(
                athletics.getId(),
                athletics.getPlayground().getId(),
                athletics.getHost().getNickname(),
                athletics.getHost().getUserProfileImg().getFileUrl(),
                athletics.getGameType().getValue(),
                athletics.getPlayground().getName(),
                athletics.getGameName(),
                Util.localDateToYearMonthDateTimeString(athletics.getGameStartDateTime()),
                athletics.getSportsEvent().getValue_cn(),
                athletics.getRunningTime(),
                athletics.getPlayground().getCampus().getCampusName()
        );
    }
}
