package com.example.playgroundmanage.vo;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.store.UploadFile;
import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
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

    @OneToMany(mappedBy = "playground")
    private List<Game> games = new ArrayList<>();

}
