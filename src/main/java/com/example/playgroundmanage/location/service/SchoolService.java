package com.example.playgroundmanage.location.service;

import com.example.playgroundmanage.date.MyDateTimeLocal;
import com.example.playgroundmanage.exception.SchoolNotExistException;
import com.example.playgroundmanage.game.dto.GameDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.location.dto.CampusResponseDto;
import com.example.playgroundmanage.location.repository.SchoolRepository;
import com.example.playgroundmanage.location.vo.Campus;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.location.vo.School;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.util.GameFinder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;

    private final GameFinder gameFinder;

    @Transactional
    public List<CampusResponseDto> getCampusInfo(Long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(SchoolNotExistException::new);

        return school.getCampus().stream()
                .map(campus -> CampusResponseDto.builder()
                        .campusId(campus.getId())
                        .campusName(campus.getCampusName())
                        .build())
                .toList();
    }

    @Transactional
    public List<GameDto> getUpcomingGamesBySchool(Long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(SchoolNotExistException::new);

        List<Game> upcomingGames = new ArrayList<>();

        List<Playground> soccerPlayground = school.getCampus().stream()
                .flatMap(campus -> getPlaygroundsBySportsEvent(campus, SportsEvent.SOCCER).stream())
                .toList();


        List<Playground> basketballPlayground = school.getCampus().stream()
                .flatMap(campus -> getPlaygroundsBySportsEvent(campus, SportsEvent.BASKETBALL).stream())
                .toList();

        upcomingGames.addAll(getUpcomingGames(soccerPlayground));
        upcomingGames.addAll(getUpcomingGames(basketballPlayground));

        return upcomingGames.stream()
                .map(Game::toGameDto)
                .toList();
    }

    private List<Game> getUpcomingGames(List<Playground> playgrounds) {
        LocalDateTime now = LocalDateTime.now();
        return playgrounds.stream()
                .flatMap(playground -> gameFinder.getUpcomingGames(playground.getGames(), playground.getGames().size(), MyDateTimeLocal.initMyDateTime(now))
                        .stream().limit(4))
                .collect(Collectors.toList());
    }


    private List<Playground> getPlaygroundsBySportsEvent(Campus campus, SportsEvent sportsEvent) {
        return campus.getPlaygrounds().stream()
                .filter(playground -> playground.getSportsEvent().equals(sportsEvent))
                .toList();
    }
}
