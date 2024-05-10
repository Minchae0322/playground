package com.example.playgroundmanage.refactoring;


import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.Access;
import jakarta.persistence.Entity;
import jakarta.persistence.FieldResult;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
                                       final LocalDateTime gameStartDateTime, final Integer runningTime, final Playground playground) {

        return FriendlyAthletics.builder()
                .id(null)
                .host(host)
                .gameName(gameName)
                .sportsEvent(sportsEvent)
                .gameStartDateTime(gameStartDateTime)
                .runningTime(runningTime)
                .playground(playground)
                .build();
    }

}
