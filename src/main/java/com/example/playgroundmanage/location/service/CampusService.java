package com.example.playgroundmanage.location.service;


import com.example.playgroundmanage.date.MyDateTimeLocal;
import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.game.dto.GameDto;
import com.example.playgroundmanage.exception.CampusNotExistException;
import com.example.playgroundmanage.exception.SchoolNotExistException;
import com.example.playgroundmanage.game.dto.GameResponseDto;
import com.example.playgroundmanage.game.repository.CampusRepository;
import com.example.playgroundmanage.game.service.GameDtoConverter;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.location.dto.CampusResponseDto;
import com.example.playgroundmanage.location.dto.PlaygroundResponseDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampusService {

    private final CampusRepository campusRepository;

    private final SchoolRepository schoolRepository;

    private final PlaygroundFinder playgroundFinder;

    private final GameFinder gameFinder;

    private final GameSorting gameSorting;

    private final GameDtoConverter gameDtoConverter;


    @Transactional
    public List<GameResponseDto> getUpcomingGamesInCampusBySportsEvent(Long campusId, SportsEvent sportsEvent) {
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(CampusNotExistException::new);

        List<Playground> playgrounds = playgroundFinder.getPlaygroundsBySportsEvent(campus.getPlaygrounds(), sportsEvent);

        List<Game> games = gameSorting.sortGamesByEarliest(getUpcomingGames(playgrounds));

        return games.stream()
                .limit(4)
                .map(gameDtoConverter::toGameResponse)
                .toList();
    }

    @Transactional
    public List<PlaygroundResponseDto> getPlaygroundByCampusAndSportsType(Long campusId, SportsEvent valueOf) {
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(CampusNotExistException::new);

        List<Playground> playgrounds = playgroundFinder.getPlaygroundsBySportsEvent(campus.getPlaygrounds(), valueOf);

        if (playgrounds.size() == 0) {
            return new ArrayList<>();
        }

        return playgrounds.stream()
                .map(this::toPlaygroundResponseDto)
                .toList();
    }



    public List<CampusResponseDto> getCampusList(Long schoolId) {
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(SchoolNotExistException::new);

        return school.getCampus().stream()
                .map(campus -> CampusResponseDto.builder()
                        .campusName(campus.getCampusName())
                        .campusId(campus.getId())
                        .build())
                .toList();
    }

    private List<Game> getUpcomingGames(List<Playground> playgrounds) {
        LocalDateTime now = LocalDateTime.now();
        return playgrounds.stream()
                .flatMap(playground -> gameFinder.getUpcomingGames(playground.getGames(), playground.getGames().size(), MyDateTimeLocal.initMyDateTime(now))
                        .stream().limit(4))
                .collect(Collectors.toList());
    }

    private PlaygroundResponseDto toPlaygroundResponseDto(Playground playground) {
        return PlaygroundResponseDto.builder()
                .playgroundId(playground.getId())
                .playgroundName(playground.getName())
                .upcomingGameNum(playground.getUpcomingGamesOrderedByStartDateTime().size())
                .sportsEvent(playground.getSportsEvent().getValue_cn())
                .playgroundProfileImg(playground.getImg().getFileUrl())
                .campusName(playground.getCampus().getCampusName())
                .schoolName(playground.getCampus().getSchool().getSchoolName())
                .build();
    }

}
