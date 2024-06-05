package com.example.playgroundmanage.location.vo;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.dto.PlaygroundDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.store.vo.UploadFile;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Playground {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    private UploadFile img;

    @ManyToOne
    private Campus campus;

    private SportsEvent sportsEvent;

    @OneToMany(mappedBy = "playground", fetch = FetchType.EAGER)
    private List<Game> games = new ArrayList<>();

    @OneToMany(mappedBy = "playground", fetch = FetchType.EAGER)
    private List<Athletics> athletics = new ArrayList<>();

    @Builder
    public Playground(Long id, String name, UploadFile img, Campus campus, SportsEvent sportsEvent, List<Game> games, List<Athletics> athletics) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.campus = campus;
        this.sportsEvent = sportsEvent;
        this.games = games;
        this.athletics = athletics;
    }




    public List<Athletics> getUpcomingGamesOrderedByStartDateTime() {
        return this.getAthletics().stream()
                .filter(game -> game.getGameStartDateTime().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Athletics::getGameStartDateTime).reversed())
                .toList();
    }




}
