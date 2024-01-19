package com.example.playgroundmanage.team.vo;

import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.team.vo.Team;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@RequiredArgsConstructor
public class Teaming {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Team team;

    private String role;

    private LocalDate createdTime;

    @Builder
    public Teaming(Long id, User user, Team team, String role, LocalDate createdTime) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.role = role;
        this.createdTime = createdTime;
    }
}
