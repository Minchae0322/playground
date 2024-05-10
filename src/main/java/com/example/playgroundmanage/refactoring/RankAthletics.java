package com.example.playgroundmanage.refactoring;

import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class RankAthletics extends Athletics {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AthleticsSide> athleticsSides;

    @Builder
    public RankAthletics(Long id,
                         String gameName,
                         SportsEvent sportsEvent,
                         GameType gameType,
                         LocalDateTime gameStartDateTime,
                         Integer runningTime,
                         Playground playground,
                         User host
    ) {
        super(id, gameName, sportsEvent, gameType, gameStartDateTime, runningTime, playground, host, new ArrayList<>());
        this.athleticsSides = new ArrayList<>();
    }

    public static RankAthletics of(final User host, final String gameName,
                                   final SportsEvent sportsEvent, final LocalDateTime gameStartDateTime,
                                   final Integer runningTime,
                                   final Playground playground) {
        return RankAthletics.builder()
                .id(null)
                .host(host)
                .gameName(gameName)
                .sportsEvent(sportsEvent)
                .gameStartDateTime(gameStartDateTime)
                .runningTime(runningTime)
                .playground(playground)
                .build();
    }

    public void addAthleticsSide(AthleticsSide athleticsSide) {
        this.athleticsSides.add(athleticsSide);
    }

}
