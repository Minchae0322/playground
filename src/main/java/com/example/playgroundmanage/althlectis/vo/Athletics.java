package com.example.playgroundmanage.althlectis.vo;

import com.example.playgroundmanage.global.BaseEntity;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.AthleticsRequest;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.JOINED) // 상속 전략 설정
@DiscriminatorColumn(name = "type") // 상속받는 클래스를 구분하는 컬럼
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Athletics extends BaseEntity implements Comparable<Athletics> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;

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

    @OneToOne(mappedBy = "athletics", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private AthleticsResult athleticsResult;

    @OneToMany(mappedBy = "athletics", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AthleticsRequest> athleticsRequests = new ArrayList<>();

    @OneToMany(mappedBy = "athletics", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AthleticsParticipant> athleticsParticipants = new ArrayList<>();

    public Athletics(Long id, String gameName, SportsEvent sportsEvent, GameType gameType, LocalDateTime gameStartDateTime, Integer runningTime, Playground playground, User host, List<AthleticsRequest> athleticsRequests, List<AthleticsParticipant> athleticsParticipants) {
        this.id = id;
        this.gameName = gameName;
        this.sportsEvent = sportsEvent;
        this.gameType = gameType;
        this.gameStartDateTime = gameStartDateTime;
        this.runningTime = runningTime;
        this.playground = playground;
        this.host = host;
        this.athleticsRequests = athleticsRequests;
        this.athleticsParticipants = athleticsParticipants;
        this.athleticsResult = AthleticsResult.builder()
                .homeScore(0)
                .awayScore(0)
                .athletics(this)
                .build();
    }
}
