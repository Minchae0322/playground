package com.example.playgroundmanage.game.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;

@Entity
@RequiredArgsConstructor
public class TeamLeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
