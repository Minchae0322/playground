package com.example.playgroundmanage.game.vo;

import com.example.playgroundmanage.dto.response.UserProfileDto;
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

    private boolean isSoloTeam;

    private boolean isAccept;

    @Builder
    public SubTeam(Long id, CompetingTeam competingTeam, Team team, List<MatchParticipant> matchParticipants, boolean isSoloTeam, boolean isAccept) {
        this.id = id;
        this.competingTeam = competingTeam;
        this.team = team;
        this.matchParticipants = matchParticipants;
        this.isSoloTeam = isSoloTeam;
        this.isAccept = isAccept;
    }

    public List<UserProfileDto> getParticipantsInfo() {
        return matchParticipants.stream()
                .map(p -> UserProfileDto.builder()
                        .userNickname(p.getUser().getNickname())
                        .userId(p.getUser().getId())
                        .build())
                .toList();
    }

    public boolean isSoloTeam() {
        return this.isSoloTeam;
    }

    public boolean isContainTeam(Team team) {
        if(isSoloTeam) {
            return false;
        }
        return this.getId() == team.getId();
    }

}
