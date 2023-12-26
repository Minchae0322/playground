package com.example.playgroundmanage;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
@Entity
@RequiredArgsConstructor
public class Test {
    @Id
    @GeneratedValue
    private Long id;


    private ZonedDateTime zonedDateTime;


    @Builder
    public Test(Long id, ZonedDateTime zonedDateTime) {
        this.id = id;
        this.zonedDateTime = zonedDateTime;
    }
}
