package com.example.playgroundmanage.althlectis.vo.impl;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.AthleticsRequest;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class RankAthletics extends Athletics {

    @Builder
    public RankAthletics(
            Long id,
            String gameName,
            SportsEvent sportsEvent,
            GameType gameType,
            LocalDateTime gameStartDateTime,
            Integer runningTime,
            Playground playground,
            User host,
            List<AthleticsRequest> athleticsRequests,
            List<AthleticsParticipant> athleticsParticipants
    ) {
        super(id, gameName, sportsEvent, gameType, gameStartDateTime, runningTime, playground, host, athleticsRequests, athleticsParticipants);
    }


    public static RankAthletics of(final User host, final String gameName,
                                   final SportsEvent sportsEvent, final LocalDateTime gameStartDateTime,
                                   final Integer runningTime, final Playground playground,
                                   final GameType gameType) {
        return RankAthletics.builder()
                .id(null)
                .host(host)
                .gameType(gameType)
                .gameName(gameName)
                .sportsEvent(sportsEvent)
                .gameStartDateTime(gameStartDateTime)
                .runningTime(runningTime)
                .playground(playground)
                .build();
    }

    @Override
    public int compareTo(Athletics athletics) {
        return athletics.getGameStartDateTime().compareTo(this.getGameStartDateTime());
    }
}
