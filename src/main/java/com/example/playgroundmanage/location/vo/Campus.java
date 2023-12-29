package com.example.playgroundmanage.location.vo;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String campusName;

    @ManyToOne
    private School school;

    @OneToMany(mappedBy = "campus")
    private List<Playground> playgrounds = new ArrayList<>();


}
