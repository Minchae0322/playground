package com.example.playgroundmanage.location.vo;


import jakarta.persistence.*;
import lombok.Builder;
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


    @Builder
    public Campus(Long id, String campusName, School school, List<Playground> playgrounds) {
        this.id = id;
        this.campusName = campusName;
        this.school = school;
        this.playgrounds = playgrounds;
    }
}
