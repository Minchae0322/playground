package com.example.playgroundmanage.vo;


import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private School school;

    @OneToMany(mappedBy = "campus")
    private List<Playground> playgrounds = new ArrayList<>();


}
