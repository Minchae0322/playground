package com.example.playgroundmanage.location.service;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.date.MyDateTimeLocal;
import com.example.playgroundmanage.game.dto.GameResponseDto;
import com.example.playgroundmanage.game.dto.GameTimeDto;
import com.example.playgroundmanage.dto.PlaygroundDto;
import com.example.playgroundmanage.dto.response.*;
import com.example.playgroundmanage.exception.CampusNotExistException;
import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.exception.SchoolNotExistException;
import com.example.playgroundmanage.game.repository.CampusRepository;
import com.example.playgroundmanage.game.service.GameDtoConverter;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.location.dto.PlaygroundResponseDto;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.repository.SchoolRepository;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.location.vo.School;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.util.*;
import com.example.playgroundmanage.location.vo.Campus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;


import static com.example.playgroundmanage.util.GameValidation.validateStartTimeAfterPresent;

@Service
@RequiredArgsConstructor
public class PlaygroundService {

    private final PlaygroundRepository playgroundRepository;

    private final GameFinder gameFinder;

    private final GameDtoConverter gameDtoConverter;

    private final GameValidation gameValidation;

    private final PlaygroundDtoConverter playgroundDtoConverter;

    private final GameSorting gameSorting;

    @Transactional
    public boolean isValidGameStartTime(Long playgroundId, GameTimeDto gameTimeDto) {
        Playground playground = playgroundRepository.findById(playgroundId)
                .orElseThrow(PlaygroundNotExistException::new);

        validateStartTimeAfterPresent(gameTimeDto);
        gameValidation.validateOverlappingGames(playground.getGames(), gameTimeDto);

        return true;
    }

    @Transactional
    public List<OccupiedTime> getPlaygroundOccupiedTimeLines(Long playgroundId, GameTimeDto gameTimeDto) {
        Playground playground = playgroundRepository.findById(playgroundId)
                .orElseThrow(PlaygroundNotExistException::new);


        //todo 일단은 queryDsl 사용하지 않고 운동장안에 모든 게임을 불러와서 filter 하는걸로
        List<Game> gamesOnSelectedDate = gameFinder.getGamesForSelectedDate(playground.getGames(), gameTimeDto.getStartDateTime());
        List<Game> orderedByEarliest = gameSorting.sortGamesByEarliest(gamesOnSelectedDate);

        return orderedByEarliest.stream()
                .map(g -> OccupiedTime.builder()
                        .start(g.getGameStartDateTime())
                        .end(g.getGameStartDateTime().plusMinutes(g.getRunningTime()))
                        .build())
                .toList();
    }


    @Transactional
    public GameResponseDto getOngoingGame(Long playgroundId) {
        Playground playground = playgroundRepository.findById(playgroundId)
                .orElseThrow(PlaygroundNotExistException::new);

        Game onGoingGame = GameFinder.findOngoingGame(playground.getGames(), MyDateTime.initMyDateTime(ZonedDateTime.now()));

        return gameDtoConverter.toGameResponse(onGoingGame);
    }

    @Transactional
    public List<GameResponseDto> getUpcomingGames(Long playgroundId) {
        Playground playground = playgroundRepository.findById(playgroundId)
                .orElseThrow(PlaygroundNotExistException::new);

        List<Game> upcomingGames = gameFinder.getUpcomingGames(playground.getGames(), playground.getGames().size(), MyDateTimeLocal.initMyDateTime(LocalDateTime.now()));

        return upcomingGames.stream()
                .map(gameDtoConverter::toGameResponse)
                .toList();
    }


    @Transactional
    public PlaygroundResponseDto getPlaygroundInfo(Long playgroundId) {
        Playground playground = playgroundRepository.findById(playgroundId)
                .orElseThrow(PlaygroundNotExistException::new);

        return playgroundDtoConverter.toPlaygroundResponseInfoDto(playground);
    }




}
