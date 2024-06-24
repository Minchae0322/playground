package com.example.playgroundmanage.location.service;

import com.example.playgroundmanage.althlectis.dto.response.AthleticsResponse;
import com.example.playgroundmanage.date.MyDateTimeLocal;
import com.example.playgroundmanage.exception.SchoolNotExistException;
import com.example.playgroundmanage.game.dto.GameDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.location.dto.CampusResponseDto;
import com.example.playgroundmanage.location.dto.response.PlaygroundInfoResponse;
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

    private final CampusService campusService;


    @Transactional
    public List<CampusResponseDto> getCampusList(Long schoolId) {
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

        for (SportsEvent event : SportsEvent.values()) {
            List<Playground> playgrounds = school.getCampus().stream()
                    .flatMap(campus -> getPlaygroundsBySportsEvent(campus, event).stream())
                    .toList();
            upcomingGames.addAll(getUpcomingGames(playgrounds));
        }

        return upcomingGames.stream()
                .map(Game::toGameDto)
                .toList();
    }

    public List<PlaygroundInfoResponse> getPlaygroundBySchoolAndSportsType(Long schoolId, SportsEvent valueOf) {
        School school = findSchoolById(schoolId);

        return school.getCampus().stream()
                .flatMap(
                        campus -> campusService.getPlaygroundByCampusAndSportsType(campus.getId(), valueOf)
                                .stream()
                )
                .toList();
    }
    @Transactional
    public List<AthleticsResponse> getUpcomingGamesBySchoolAndSportsEvent(Long schoolId, SportsEvent sportsEvent) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(SchoolNotExistException::new);

        return school.getCampus().stream()
                .flatMap(campus -> campusService.getUpcomingGamesInCampusBySportsEvent(campus.getId(), sportsEvent).stream())
                .toList();
    }


    private List<Game> getUpcomingGames(List<Playground> playgrounds) {
        LocalDateTime now = LocalDateTime.now();
        return playgrounds.stream()
                .flatMap(playground -> gameFinder.getUpcomingGames(playground.getGames(), playground.getGames().size(), MyDateTimeLocal.initMyDateTime(now))
                        .stream().limit(4))
                .collect(Collectors.toList());
    }

    private School findSchoolById(Long schoolId) {
        return schoolRepository.findById(schoolId)
                .orElseThrow(SchoolNotExistException::new);
    }


    private List<Playground> getPlaygroundsBySportsEvent(Campus campus, SportsEvent sportsEvent) {
        return campus.getPlaygrounds().stream()
                .filter(playground -> playground.getSportsEvent().equals(sportsEvent))
                .toList();
    }


}
