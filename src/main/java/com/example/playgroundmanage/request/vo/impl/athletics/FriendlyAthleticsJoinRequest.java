package com.example.playgroundmanage.request.vo.impl.athletics;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.impl.FriendlyAthletics;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.AthleticsRequest;
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
public class FriendlyAthleticsJoinRequest extends AthleticsRequest {

    @ManyToOne(cascade = CascadeType.PERSIST)
    private FriendlyAthletics friendlyAthletics;
    @Builder
    public FriendlyAthleticsJoinRequest(Long id, User user, User host, RequestState requestState, GameTeamSide gameTeamSide, FriendlyAthletics friendlyAthletics) {
        super(id, user, host, requestState, gameTeamSide, friendlyAthletics);
        this.friendlyAthletics = friendlyAthletics;
    }

    public static FriendlyAthleticsJoinRequest of(
            final FriendlyAthletics friendlyAthletics,
            final User user,
            final User host,
            final RequestState requestState
    ) {

        return FriendlyAthleticsJoinRequest.builder()
                .user(user)
                .host(host)
                .friendlyAthletics(friendlyAthletics)
                .requestState(requestState)
                .gameTeamSide(GameTeamSide.NONE)
                .build();
    }



}
