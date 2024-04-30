package com.example.playgroundmanage.refactoring;

import com.example.playgroundmanage.date.DateTime;
import com.example.playgroundmanage.game.dto.GameTimeDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;

public record GameGenerationRequest(
        Long gameId,
        Long playgroundId,
        User host,
        GameType gameType,
        Playground playground,
        String gameName,
        Integer participantNum,
        DateTime startDateTime,
        SportsEvent sportsEvent,
        Integer runningTime) {

    public GameTimeDto toGameTimeDto() {
        return GameTimeDto.builder()
                .runningTime(runningTime)
                .startDateTime(startDateTime)
                .build();
    }

    public Athletics toEntity() {
        return RankAthletics.builder()
                .gameName(gameName)
                .host(host)
                .playground(playground)
                .gameType(GameType.COMPETITION)
                .gameStartDateTime(startDateTime.getLocalDateTime())
                .sportsEvent(sportsEvent)
                .runningTime(runningTime)
                .build();
    }

}
