package com.example.playgroundmanage.request.vo;


import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.global.BaseEntity;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.impl.TeamJoinRequest;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.type.RequestState;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TeamRequest extends BaseEntity implements Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RequestState requestState;

    @ManyToOne
    private User user;

    @ManyToOne
    private User leader;

    @ManyToOne
    private Team team;

    private String introduction;


    public TeamRequest(Long id, RequestState requestState, User user, User leader, Team team, String introduction) {
        this.id = id;
        this.requestState = requestState;
        this.user = user;
        this.leader = leader;
        this.team = team;
        this.introduction = introduction;
    }

    public TeamJoinRequest update(String introduction) {
        this.introduction = introduction;
        return (TeamJoinRequest) this;
    }
}
