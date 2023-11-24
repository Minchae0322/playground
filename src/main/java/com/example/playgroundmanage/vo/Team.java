package com.example.playgroundmanage.vo;

import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;

    private String teamPic;

    @Enumerated
    private SportsEvent sportsEvent;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MatchParticipantTeam> matchParticipantTeams = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Teaming> members = new ArrayList<>();

    @Builder
    public Team(Long id, String teamName, String teamPic, SportsEvent sportsEvent, List<MatchParticipantTeam> matchParticipantTeams, List<Teaming> members) {
        this.id = id;
        this.teamName = teamName;
        this.teamPic = teamPic;
        this.sportsEvent = sportsEvent;
        this.matchParticipantTeams = matchParticipantTeams;
        this.members = members;
    }
}
