package com.example.playgroundmanage.vo;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.store.UploadFile;
import jakarta.persistence.*;
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

    @OneToMany(mappedBy = "playground", fetch = FetchType.EAGER)
    private List<Game> games = new ArrayList<>();

    public List<Game> orderedByLatest(List<Game> games) {
        List<Game> notStartedGames = games.stream()
                .filter(g -> g.getGameStartDateTime().isAfter(LocalDateTime.now()))
                .toList();
        notStartedGames.sort(Comparator.comparing(Game::getGameStartDateTime).reversed());
        return notStartedGames;
    }


}
