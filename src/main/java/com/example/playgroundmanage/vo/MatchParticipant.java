package com.example.playgroundmanage.vo;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class MatchParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    private MatchTeam matchTeam;

    @Builder
    public MatchParticipant(Long id, User user, MatchTeam matchTeam) {
        this.id = id;
        this.user = user;
        this.matchTeam = matchTeam;
    }
}
