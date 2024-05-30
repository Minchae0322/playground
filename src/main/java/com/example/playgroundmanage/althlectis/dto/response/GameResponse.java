package com.example.playgroundmanage.althlectis.dto.response;

import com.example.playgroundmanage.althlectis.vo.Athletics;

import static com.example.playgroundmanage.util.Util.localDateToYearMonthDateTimeString;

public record GameResponse(
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

    public static GameResponse of(Athletics athletics) {
        return new GameResponse(
                athletics.getId(),
                athletics.getPlayground().getId(),
                athletics.getHost().getNickname(),
                athletics.getHost().getUserProfileImg().getFileUrl(),
                athletics.getGameType().getValue_cn(),
                athletics.getPlayground().getName(),
                athletics.getGameName(),
                localDateToYearMonthDateTimeString(athletics.getGameStartDateTime()),
                athletics.getSportsEvent().getValue_cn(),
                athletics.getRunningTime(),
                athletics.getPlayground().getCampus().getCampusName()
        );
    }
}
