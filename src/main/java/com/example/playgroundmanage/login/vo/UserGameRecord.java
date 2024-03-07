package com.example.playgroundmanage.login.vo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
@Getter
public class UserGameRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    private int win;

    private int lose;

    private int draw;

    private int none;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastUpdateTime;


    @Builder
    public UserGameRecord(Long id, User user, int win, int lose, int draw, int none, LocalDateTime lastUpdateTime) {
        this.id = id;
        this.user = user;
        this.win = win;
        this.lose = lose;
        this.draw = draw;
        this.none = none;
        this.lastUpdateTime = lastUpdateTime;
    }


    public UserGameRecord update(int win, int lose) {
        this.win = win;
        this.lose = lose;
        this.lastUpdateTime = LocalDateTime.now();
        return this;
    }

}
