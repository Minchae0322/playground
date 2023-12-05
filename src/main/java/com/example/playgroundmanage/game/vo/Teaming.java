package com.example.playgroundmanage.game.vo;

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

    private LocalDate createdTime;

    @Builder
    public Teaming(Long id, User user, Team team) {
        this.id = id;
        this.user = user;
        this.team = team;
        this.createdTime = LocalDate.now();
    }
}
