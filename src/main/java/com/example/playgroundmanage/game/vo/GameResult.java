package com.example.playgroundmanage.game.vo;

import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Entity
@Getter
public class GameResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    private Game game;

    private int homeScore;

    private int awayScore;

    @Builder
    public GameResult(Long id, Game game, int homeScore, int awayScore) {
        this.id = id;
        this.game = game;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
    }

    public void updateResult(GameResult gameResult) {
        this.homeScore = gameResult.getHomeScore();
        this.awayScore = gameResult.getAwayScore();
    }
}
