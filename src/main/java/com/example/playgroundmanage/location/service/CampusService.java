package com.example.playgroundmanage.location.service;


import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.exception.CampusNotExistException;
import com.example.playgroundmanage.exception.SchoolNotExistException;
import com.example.playgroundmanage.game.repository.CampusRepository;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.repository.SchoolRepository;
import com.example.playgroundmanage.location.vo.Campus;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.location.vo.School;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.util.GameFinder;
import com.example.playgroundmanage.util.GameSorting;
import com.example.playgroundmanage.util.PlaygroundFinder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.playgroundmanage.date.MyDateTimeLocal.initMyDateTime;

@Service
@RequiredArgsConstructor
public class CampusService {
    private final PlaygroundRepository playgroundRepository;

    private final CampusRepository campusRepository;

    private final PlaygroundFinder playgroundFinder;

    private final GameFinder gameFinder;

    private final GameSorting gameSorting;

    private final SchoolRepository schoolRepository;

    @Transactional
    public List<GameDto> getUpcomingGamesInCampusBySportsEvent(Long campusId, SportsEvent sportsEvent) {
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(CampusNotExistException::new);

        List<Playground> playgrounds = playgroundFinder.getPlaygroundsBySportsEvent(campus.getPlaygrounds(), sportsEvent);

        List<Game> games = gameSorting.sortGamesByEarliest(getUpcomingGames(playgrounds));

        return games.stream()
                .limit(3)
                .map(Game::toGameDto)
                .toList();
    }

    @Transactional
    public List<GameDto> getUpcomingGamesBySportsEvent(Long schoolId, SportsEvent sportsEvent) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(SchoolNotExistException::new);

        return school.getCampus().stream()
                .flatMap(campus -> getUpcomingGamesInCampusBySportsEvent(campus.getId(), sportsEvent).stream())
                .toList();
    }

    @Transactional
    public List<GameDto> getUpcomingGamesBySchool(Long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(SchoolNotExistException::new);

        List<Game> upcomingGames = school.getCampus().stream()
                .flatMap(campus -> getUpcomingGames(campus.getPlaygrounds()).stream())
                .toList();

        return upcomingGames.stream()
                .limit(4)
                .map(Game::toGameDto)
                .toList();
    }

    private List<Game> getUpcomingGames(List<Playground> playgrounds) {
        LocalDateTime now = LocalDateTime.now();
        return playgrounds.stream()
                .flatMap(playground -> gameFinder.getUpcomingGames(playground.getGames(), playground.getGames().size(), initMyDateTime(now))
                        .stream())
                .collect(Collectors.toList());
    }

}
