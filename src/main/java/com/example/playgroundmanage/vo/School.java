package com.example.playgroundmanage.vo;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schoolName;

    private String schoolImg;

    @OneToMany(mappedBy = "school")
    private List<Campus> campus = new ArrayList<>();

}
