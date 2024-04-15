package com.example.playgroundmanage.team.vo;

import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.request.vo.TeamRequest;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.store.vo.UploadFile;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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
    @ColumnDefault("")
    private UploadFile teamProfileImg;

    @ManyToOne
    private User leader;

    private String description;

    @Enumerated(EnumType.STRING)
    private SportsEvent sportsEvent;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SubTeam> subTeams = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Teaming> members = new ArrayList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamRequest> teamRequests = new ArrayList<>();


    @Builder
    public Team(Long id, String teamName, UploadFile teamProfileImg, User leader, String description, SportsEvent sportsEvent, List<SubTeam> subTeams, List<Teaming> members) {
        validate(teamName, description);
        this.id = id;
        this.teamName = teamName;
        this.teamProfileImg = teamProfileImg;
        this.leader = leader;
        this.description = description;
        this.sportsEvent = sportsEvent;
        this.subTeams = subTeams;
        this.members = members;
    }

    private void validate(String teamName, String description) {
        if (teamName.length() > 9 || teamName.equals("")) {
            throw new RuntimeException("不符合格式。 最大字数9个字");
        }
        if (description.length() > 40) {
            throw new RuntimeException("不符合格式。 最大字数40个字");
        }
    }

    public void updateTeamPic(UploadFile uploadFile) {
        this.teamProfileImg = uploadFile;
    }

}
