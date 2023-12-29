package com.example.playgroundmanage.location.vo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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
