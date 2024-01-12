package com.example.playgroundmanage.game.vo;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class GameParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private SubTeam subTeam;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Game game;

    private boolean isAccepted;

    @Builder
    public GameParticipant(Long id, User user, SubTeam subTeam, Game game, boolean isAccepted) {
        this.id = id;
        this.user = user;
        this.subTeam = subTeam;
        this.game = game;
        this.isAccepted = isAccepted;
    }
}
