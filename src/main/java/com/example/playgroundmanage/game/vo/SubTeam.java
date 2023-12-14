package com.example.playgroundmanage.game.vo;

import com.example.playgroundmanage.dto.response.UserInfoDto;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
public class SubTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private CompetingTeam competingTeam;

    @ManyToOne
    private Team team;

    @OneToMany(mappedBy = "subTeam", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MatchParticipant> matchParticipants = new ArrayList<>();

    private boolean isNoneTeam;

    private boolean isAccept;

    @Builder
    public SubTeam(Long id, CompetingTeam competingTeam, Team team, List<MatchParticipant> matchParticipants, boolean isNoneTeam, boolean isAccept) {
        this.id = id;
        this.competingTeam = competingTeam;
        this.team = team;
        this.matchParticipants = matchParticipants;
        this.isNoneTeam = isNoneTeam;
        this.isAccept = isAccept;
    }

    public List<UserInfoDto> getParticipantsInfo() {
        return matchParticipants.stream()
                .map(p -> UserInfoDto.builder()
                        .userNickname(p.getUser().getNickname())
                        .userId(p.getUser().getId())
                        .build())
                .toList();
    }

    public boolean isSoloTeam() {
        return this.isNoneTeam;
    }

    public boolean isContainTeam(Team team) {
        if(isNoneTeam) {
            return false;
        }
        return this.getId() == team.getId();
    }
}
