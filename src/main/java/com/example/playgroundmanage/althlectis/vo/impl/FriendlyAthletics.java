package com.example.playgroundmanage.althlectis.vo.impl;


import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.AthleticsRequest;
import com.example.playgroundmanage.request.vo.impl.athletics.FriendlyAthleticsJoinRequest;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
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
    public FriendlyAthletics(Long id, String gameName, SportsEvent sportsEvent, GameType gameType, LocalDateTime gameStartDateTime, Integer runningTime, Playground playground, User host, List<AthleticsRequest> athleticsRequests, List<AthleticsParticipant> athleticsParticipants) {
        super(id, gameName, sportsEvent, gameType, gameStartDateTime, runningTime, playground, host, athleticsRequests, athleticsParticipants);
    }

    public static FriendlyAthletics of(
            final User host, final String gameName, final SportsEvent sportsEvent,
            final LocalDateTime gameStartDateTime, final Integer runningTime, final Playground playground,
            final GameType gameType
    ) {

        return FriendlyAthletics.builder()
                .id(null)
                .host(host)
                .gameName(gameName)
                .gameType(gameType)
                .sportsEvent(sportsEvent)
                .gameStartDateTime(gameStartDateTime)
                .runningTime(runningTime)
                .athleticsParticipants(new ArrayList<>())
                .athleticsRequests(new ArrayList<>())
                .playground(playground)
                .build();
    }



    @Override
    public int compareTo(Athletics athletics) {
        return athletics.getGameStartDateTime().compareTo(this.getGameStartDateTime());
    }
}
