package com.example.playgroundmanage.vo;

import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teamName;

    private String teamPic;

    @Enumerated
    private SportsEvent sportsEvent;

    @OneToMany(mappedBy = "team")
    private List<Match> matchResult;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<MatchParticipantTeam> matchParticipantTeams = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Teaming> members = new ArrayList<>();
}
