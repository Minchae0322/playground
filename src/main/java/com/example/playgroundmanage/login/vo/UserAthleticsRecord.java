package com.example.playgroundmanage.login.vo;

import com.example.playgroundmanage.global.BaseEntity;
import com.example.playgroundmanage.type.GameRecord;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
@Getter
public class UserAthleticsRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    private int win;

    private int lose;

    private int draw;

    private int none;


    @Builder
    public UserAthleticsRecord(Long id, User user, int win, int lose, int draw, int none) {
        this.id = id;
        this.user = user;
        this.win = win;
        this.lose = lose;
        this.draw = draw;
        this.none = none;

    }


    public UserAthleticsRecord update(int win, int lose, int draw) {
        this.win = win;
        this.lose = lose;
        this.draw = draw;
        return this;
    }

    public UserAthleticsRecord update(GameRecord gameRecord) {
        if (gameRecord.equals(GameRecord.WIN)) {
            this.win++;
        } else if (gameRecord.equals(GameRecord.LOSE)) {
            this.lose++;
        } else if (gameRecord.equals(GameRecord.DRAW)) {
            this.draw++;
        } else {
            this.none++;
        }
        return this;
    }

}
