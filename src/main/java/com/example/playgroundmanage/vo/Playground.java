package com.example.playgroundmanage.vo;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.store.UploadFile;
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

    @Builder
    public Playground(Long id, String name, UploadFile img, Campus campus, SportsEvent sportsEvent, List<Game> games) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.campus = campus;
        this.sportsEvent = sportsEvent;
        this.games = games;
    }




    public List<Game> orderedByLatest(List<Game> games) {
        List<Game> notStartedGames = games.stream()
                .filter(g -> g.getGameStartDateTime().isAfter(LocalDateTime.now()))
                .toList();
        notStartedGames.sort(Comparator.comparing(Game::getGameStartDateTime).reversed());
        return notStartedGames;
    }


}
