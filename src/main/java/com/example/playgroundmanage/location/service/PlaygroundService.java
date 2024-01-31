package com.example.playgroundmanage.location.service;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.dto.GameTimeDto;
import com.example.playgroundmanage.dto.PlaygroundDto;
import com.example.playgroundmanage.dto.response.*;
import com.example.playgroundmanage.exception.CampusNotExistException;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.exception.SchoolNotExistException;
import com.example.playgroundmanage.game.repository.CampusRepository;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.location.dto.PlaygroundResponseDto;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.repository.SchoolRepository;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.location.vo.School;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.util.*;
import com.example.playgroundmanage.location.vo.Campus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;


import static com.example.playgroundmanage.util.GameValidation.validateStartBeforePresent;

@Service
@RequiredArgsConstructor
public class PlaygroundService {

    private final PlaygroundRepository playgroundRepository;

    private final FileHandler fileHandler;

    private final SchoolRepository schoolRepository;

    private final GameRepository gameRepository;

    private final CampusRepository campusRepository;

    private final GameFinder gameFinder;

    private final PlaygroundFinder playgroundFinder;

    private final GameValidation gameValidation;

    private final GameSorting gameSorting;

    @Transactional
    public boolean isValidGameStartTime(Long playgroundId, GameTimeDto gameTimeDto) {
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow(PlaygroundNotExistException::new);

        validateStartBeforePresent(gameTimeDto);
        gameValidation.validateOverlappingGames(playground.getGames(), gameTimeDto);

        return true;
    }

    @Transactional
    public List<OccupiedTime> getPlaygroundOccupiedTimeLines(Long playgroundId, GameTimeDto gameTimeDto) {
        Playground playground = playgroundRepository.findById(playgroundId)
                .orElseThrow(PlaygroundNotExistException::new);

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
    public GameDto getOngoingGame(Long playgroundId) {
        Playground playground = playgroundRepository.findById(playgroundId)
                .orElseThrow(PlaygroundNotExistException::new);

        Game onGoingGame = GameFinder.findOngoingGame(playground.getGames(), MyDateTime.initMyDateTime(ZonedDateTime.now()));

        return onGoingGame.toGameDto();
    }

    @Transactional
    public List<GameDto> getUpcomingGames(Long playgroundId) {
        Playground playground = playgroundRepository.findById(playgroundId)
                .orElseThrow(PlaygroundNotExistException::new);

        List<Game> upcomingGames = gameFinder.getUpcomingGames(playground.getGames(), playground.getGames().size(), MyDateTime.initMyDateTime(ZonedDateTime.now()));

        return upcomingGames.stream()
                .map(Game::toGameDto)
                .toList();
    }



    @Transactional
    public PlaygroundDto getPlaygroundInfo(Long playgroundId) {
        Playground playground = playgroundRepository.findById(playgroundId)
                .orElseThrow(PlaygroundNotExistException::new);

        return playground.toPlaygroundDto();
    }


    @Transactional
    public InMemoryMultipartFile getPlaygroundImg(Long playgroundId) {
        Playground playground = playgroundRepository.findById(playgroundId)
                .orElseThrow(PlaygroundNotExistException::new);

        return fileHandler.extractFile(playground.getImg());
    }

    @Transactional
    public List<PlaygroundDto> getPlaygroundByCampus(Long campusId) {
        Campus campus = campusRepository.findById(campusId).orElseThrow();
        return campus.getPlaygrounds().stream()
                .map(Playground::toPlaygroundDto)
        .toList();
    }



    @Transactional
    public boolean isExistGameTime(Long playgroundId, LocalDateTime day,Integer runningTime) {
        //TODO 모든 게임을 불러오기에는 너무 많으니 그 날과 그 다음날까지만 불러오기.
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow();
        List<Game> games = playground.getGames();
        return games.stream()
                .filter(g -> gameFinder.isDateTimeRangeOverlapping(g, day, runningTime))
                .toList().size() != 0;
    }

    @Transactional
    public List<PlaygroundResponseDto> getPlaygroundByCampusAndSportsType(Long campusId, SportsEvent valueOf) {
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(CampusNotExistException::new);
        List<Playground> playgrounds = playgroundFinder.getPlaygroundsBySportsEvent(campus.getPlaygrounds(), valueOf);
        if (playgrounds.size() == 0) {
            throw new PlaygroundNotExistException();
        }
        return playgrounds.stream()
                .map(playground -> PlaygroundResponseDto.builder()
                        .playgroundId(playground.getId())
                        .playgroundName(playground.getName())
                        .sportsEvent(playground.getSportsEvent().getValue())
                        .playgroundProfileImg(fileHandler.extractFile(playground.getImg()))
                        .campusName(playground.getCampus().getCampusName())
                        .schoolName(playground.getCampus().getSchool().getSchoolName())
                        .build())
                .toList();
    }

    @Transactional
    public List<PlaygroundResponseDto> getPlaygroundBySportsType(Long schoolId, SportsEvent valueOf) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(SchoolNotExistException::new);

        return school.getCampus().stream()
                .flatMap(campus -> getPlaygroundByCampusAndSportsType(campus.getId(), valueOf).stream())
                .toList();
    }

}
