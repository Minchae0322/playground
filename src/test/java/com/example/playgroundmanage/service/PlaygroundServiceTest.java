package com.example.playgroundmanage.service;

import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.repository.PlaygroundRepository;
import com.example.playgroundmanage.vo.Playground;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlaygroundServiceTest {

    @Autowired
    private PlaygroundRepository playgroundRepository;

    @Autowired
    private PlaygroundService playgroundService;

    @Autowired
    private GameRepository gameRepository;

    private Playground testPlayground;

    @BeforeEach
    void before() {

    }
}