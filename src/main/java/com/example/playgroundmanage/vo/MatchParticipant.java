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
    private SmallTeam smallTeam;

    @Builder
    public MatchParticipant(Long id, User user, SmallTeam smallTeam) {
        this.id = id;
        this.user = user;
        this.smallTeam = smallTeam;
    }
}
