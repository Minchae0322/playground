/*
package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.game.dto.GameDto;
import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.location.vo.Playground;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.ZonedDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GameServiceTest2 {

    @Mock
    private PlaygroundRepository playgroundRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private Playground playground;


    @BeforeEach
    void setUp() {
        playground = Playground.builder()
                .name("test")
                .id(1L)
                .build();; // assume this is a properly set up playground object

        when(playgroundRepository.findById(anyLong())).thenReturn(Optional.of(playground));
    }

    @Test
    void generateGame_Success() {
        // Arrange
        GameDto gameDto = GameDto.builder()
                .startDateTime(MyDateTime.initMyDateTime(ZonedDateTime.now().plusMinutes(10)))
                .gameName("name")
                .runningTime(60)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        Long playgroundId = 1L;
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Long gameId = gameService.generateGame(playgroundId, gameDto);

        // Assert
        verify(gameRepository).save(any(Game.class));
        verify(playgroundRepository).findById(playgroundId);
    }

    @Test
    void generateGame_Failure_overlappingStartTime() {
        GameDto gameDto = GameDto.builder()
                .startDateTime(MyDateTime.initMyDateTime(ZonedDateTime.now().plusMinutes(10)))
                .gameName("name")
                .runningTime(60)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        GameDto gameDto2 = GameDto.builder()
                .startDateTime(MyDateTime.initMyDateTime(ZonedDateTime.now().plusMinutes(12)))
                .gameName("name")
                .runningTime(60)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        // Arrange
        Long playgroundId = 1L;
        when(playgroundRepository.findById(playgroundId)).thenReturn(Optional.of(playground));
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        gameService.generateGame(playgroundId, gameDto);

        verify(gameRepository).save(any(Game.class));
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            gameService.generateGame(playgroundId, gameDto2);
        });

        verify(gameRepository, times(2)).save(any(Game.class));
        verify(playgroundRepository).findById(playgroundId);
    }

    @Test
    void generateGame_Failure_PlaygroundNotExist() {
        GameDto gameDto = GameDto.builder()
                .startDateTime(MyDateTime.initMyDateTime(ZonedDateTime.now().plusMinutes(10)))
                .gameName("name")
                .runningTime(60)
                .sportsEvent(SportsEvent.SOCCER)
                .build();
        // Arrange
        Long invalidPlaygroundId = 2L;
        when(playgroundRepository.findById(invalidPlaygroundId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PlaygroundNotExistException.class, () -> {
            gameService.generateGame(invalidPlaygroundId, gameDto);
        });
    }
}*/
