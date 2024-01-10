package com.example.playgroundmanage.game.vo;

import com.example.playgroundmanage.store.UploadFile;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String teamName;

    @OneToOne
    private UploadFile teamPic;

    @ManyToOne
    private User leader;

    private String description;
    @Enumerated
    private SportsEvent sportsEvent;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SubTeam> subTeams = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Teaming> members = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamRequest> teamRequests = new ArrayList<>();


    @Builder
    public Team(Long id, String teamName, UploadFile teamPic, User leader, String description, SportsEvent sportsEvent, List<SubTeam> subTeams, List<Teaming> members) {
        this.id = id;
        this.teamName = teamName;
        this.teamPic = teamPic;
        this.leader = leader;
        this.description = description;
        this.sportsEvent = sportsEvent;
        this.subTeams = subTeams;
        this.members = members;
    }


    public void updateTeamPic(UploadFile uploadFile) {
        this.teamPic = uploadFile;
    }

    public void addMember(Teaming teaming) {
        this.members = new ArrayList<>();
        this.members.add(teaming);
    }
}
