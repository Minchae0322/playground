package com.example.playgroundmanage.location.vo;

import com.example.playgroundmanage.althlectis.vo.Athletics;


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

    @Version
    private Long version;

    private String name;

    @OneToOne
    private UploadFile img;

    @ManyToOne
    private Campus campus;

    private SportsEvent sportsEvent;

    @OneToMany(mappedBy = "playground", fetch = FetchType.EAGER)
    private List<Athletics> athletics = new ArrayList<>();

    @Builder
    public Playground(Long id, String name, UploadFile img, Campus campus, SportsEvent sportsEvent, List<Athletics> athletics) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.campus = campus;
        this.sportsEvent = sportsEvent;
        this.athletics = athletics;
    }









}
