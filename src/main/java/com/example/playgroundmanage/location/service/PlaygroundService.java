package com.example.playgroundmanage.location.service;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.dto.GameTimeDto;
import com.example.playgroundmanage.dto.PlaygroundDto;
import com.example.playgroundmanage.dto.response.*;
import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.game.repository.CampusRepository;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.util.GameFinder;
import com.example.playgroundmanage.util.Util;
import com.example.playgroundmanage.location.vo.Campus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import static com.example.playgroundmanage.util.GameValidation.validateOverlappingGames;
import static com.example.playgroundmanage.util.GameValidation.validateStartBeforePresent;

@Service
@RequiredArgsConstructor
public class PlaygroundService {

    private final PlaygroundRepository playgroundRepository;

    private final FileHandler fileHandler;

    private final CampusRepository campusRepository;

    @Transactional
    public boolean isValidGameStartTime(Long playgroundId, GameTimeDto gameTimeDto) {
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow(PlaygroundNotExistException::new);

        validateStartBeforePresent(gameTimeDto);
        validateOverlappingGames(playground.getGames(), gameTimeDto);

        return true;
    }

    @Transactional
    public List<OccupiedTime> getPlaygroundOccupiedTimeLines(Long playgroundId, GameTimeDto gameTimeDto) {
        Playground playground = playgroundRepository.findById(playgroundId)
                .orElseThrow(PlaygroundNotExistException::new);

        List<Game> gamesOnSelectedDate = GameFinder.getGamesForSelectedDate(playground.getGames(), gameTimeDto.getStartDateTime());

        return gamesOnSelectedDate.stream()
                .map(g -> OccupiedTime.builder()
                        .start(g.getGameStartDateTime())
                        .end(g.getGameEndDateTime())
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
    public List<GameDto> getUpcomingGames(Long playgroundId, Integer numberOfGame) {
        Playground playground = playgroundRepository.findById(playgroundId)
                .orElseThrow(PlaygroundNotExistException::new);

        List<Game> upcomingGames = GameFinder.getUpcomingGames(playground.getGames(), numberOfGame, MyDateTime.initMyDateTime(ZonedDateTime.now()));

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
    public InMemoryMultipartFile getPlaygroundImg(Long playgroundId) throws IOException {
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

    public List<GameThumbnail> getGamesThumbnailOrderedByLatest(Long playgroundId) {
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow();
        List<Game> notStartedGames =  playground.getUpcomingGamesOrderedByStartDateTime();
        return notStartedGames.stream()
                .map(g -> GameThumbnail.builder()
                        .hostName(g.getHost().getNickname())
                        .gameStart(Util.localDateToYearMonthDateTimeString(g.getGameStartDateTime()))
                        .runningTime(g.getRunningTime())
                        .sportsEvent(g.getSportsEvent())
                        .build())
                .toList();
    }


    @Transactional
    public boolean isExistGameTime(Long playgroundId, LocalDateTime day,Integer runningTime) {
        //TODO 모든 게임을 불러오기에는 너무 많으니 그 날과 그 다음날까지만 불러오기.
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow();
        List<Game> games = playground.getGames();
        return games.stream()
                .filter(g -> g.isTimeRangeOverlapping(day, runningTime))
                .toList().size() != 0;
    }

}
