/*
package com.example.playgroundmanage.game.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.example.playgroundmanage.game.GameResultInjection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class GameResultInjectionTest {


    @InjectMocks
    private GameResultInjection gameService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInjectResult() {
        // Given
        Game game = createTestGame();
        //GameResult gameResult = new GameResult(1, 2, 1);

        // When
        //gameService.injectResult(game, gameResult);

        // Then
        //verify(gameParticipantRepository, times(2)).saveAll(anyList());
    }

    private Game createTestGame() {
        // Implement the creation of a test game for the given scenario
        // You may use a mocking framework or create a simple test instance
        // ...

        return null;
    }
}
*/
