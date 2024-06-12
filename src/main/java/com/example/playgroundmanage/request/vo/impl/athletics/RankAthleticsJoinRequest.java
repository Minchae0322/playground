package com.example.playgroundmanage.request.vo.impl.athletics;


import com.example.playgroundmanage.althlectis.vo.impl.FriendlyAthletics;
import com.example.playgroundmanage.althlectis.vo.impl.RankAthletics;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.AthleticsRequest;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.RequestState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RankAthleticsJoinRequest extends AthleticsRequest {

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Team team;

    @Builder
    public RankAthleticsJoinRequest(Long id, User user, User host, RequestState requestState, GameTeamSide gameTeamSide, RankAthletics rankAthletics, Team team) {
        super(id, user, host, requestState, gameTeamSide, rankAthletics);
        this.team = team;
    }

    public static RankAthleticsJoinRequest of(
            final RankAthletics rankAthletics,
            final User user,
            final User host,
            final RequestState requestState,
            final Team team
    ) {

        return RankAthleticsJoinRequest.builder()
                .user(user)
                .host(host)
                .rankAthletics(rankAthletics)
                .requestState(requestState)
                .gameTeamSide(GameTeamSide.NONE)
                .team(team)
                .build();
    }

    public static RankAthleticsJoinRequest of(
            final RankAthletics rankAthletics,
            final User user,
            final User host,
            final RequestState requestState
    ) {

        return RankAthleticsJoinRequest.builder()
                .user(user)
                .host(host)
                .rankAthletics(rankAthletics)
                .requestState(requestState)
                .gameTeamSide(GameTeamSide.NONE)
                .build();
    }
}
