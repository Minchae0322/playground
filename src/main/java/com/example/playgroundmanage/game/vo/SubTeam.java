package com.example.playgroundmanage.game.vo;

import com.example.playgroundmanage.dto.response.UserInfoDto;
import com.example.playgroundmanage.team.vo.Team;
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

    @OneToMany(mappedBy = "subTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameParticipant> gameParticipants = new ArrayList<>();

    private boolean isSoloTeam;

    private boolean isAccept;

    @Builder
    public SubTeam(Long id, CompetingTeam competingTeam, Team team, List<GameParticipant> gameParticipants, boolean isSoloTeam, boolean isAccept) {
        this.id = id;
        this.competingTeam = competingTeam;
        this.team = team;
        this.gameParticipants = gameParticipants;
        this.isSoloTeam = isSoloTeam;
        this.isAccept = isAccept;
    }

    public List<UserInfoDto> getParticipantsInfo() {
        return gameParticipants.stream()
                .map(p -> UserInfoDto.builder()
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

    public String getTeamName() {
        if (isSoloTeam) {
            return "Solo";
        }
        return team.getTeamName();
    }

}
