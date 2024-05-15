package com.example.playgroundmanage.althlectis.vo;


import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendlyAthletics extends Athletics {

    @Builder
    public FriendlyAthletics(
            Long id,
            User host,
            String gameName,
            SportsEvent sportsEvent,
            GameType gameType,
            LocalDateTime gameStartDateTime,
            Integer runningTime,
            Playground playground
    ) {
        super(id, gameName, sportsEvent, gameType, gameStartDateTime, runningTime, playground, host, new ArrayList<>());
    }

    public static FriendlyAthletics of(final User host, final String gameName, final SportsEvent sportsEvent,
                                       final LocalDateTime gameStartDateTime, final Integer runningTime, final Playground playground,
                                       final GameType gameType) {

        return FriendlyAthletics.builder()
                .id(null)
                .host(host)
                .gameName(gameName)
                .gameType(gameType)
                .sportsEvent(sportsEvent)
                .gameStartDateTime(gameStartDateTime)
                .runningTime(runningTime)
                .playground(playground)
                .build();
    }

}
