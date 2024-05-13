package com.example.playgroundmanage.refactoring;

import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED) // 상속 전략 설정
@DiscriminatorColumn(name = "type") // 상속받는 클래스를 구분하는 컬럼
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Athletics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String gameName;

    @Enumerated(value = EnumType.STRING)
    private SportsEvent sportsEvent;

    @Enumerated
    private GameType gameType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime gameStartDateTime;

    private Integer runningTime;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Playground playground;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User host;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<GameParticipant> gameParticipants;

    public Athletics(Long id, String gameName, SportsEvent sportsEvent, GameType gameType, LocalDateTime gameStartDateTime, Integer runningTime, Playground playground, User host, List<GameParticipant> gameParticipants) {
        this.id = id;
        this.gameName = gameName;
        this.sportsEvent = sportsEvent;
        this.gameType = gameType;
        this.gameStartDateTime = gameStartDateTime;
        this.runningTime = runningTime;
        this.playground = playground;
        this.host = host;
        this.gameParticipants = gameParticipants;
    }




}
