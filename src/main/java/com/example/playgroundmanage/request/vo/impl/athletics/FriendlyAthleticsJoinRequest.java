package com.example.playgroundmanage.request.vo.impl.athletics;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.AthleticsRequest;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.RequestState;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FriendlyAthleticsJoinRequest extends AthleticsRequest {

    @Builder
    public FriendlyAthleticsJoinRequest(Long id, Athletics athletics, User user, User host, RequestState requestState, GameTeamSide gameTeamSide) {
        super(id, athletics, user, host, requestState, gameTeamSide);
    }

    public static FriendlyAthleticsJoinRequest of(
            final Athletics athletics,
            final User user,
            final User host,
            final RequestState requestState
    ) {

        return FriendlyAthleticsJoinRequest.builder()
                .athletics(athletics)
                .user(user)
                .host(host)
                .requestState(requestState)
                .gameTeamSide(GameTeamSide.NONE)
                .build();
    }

}
