package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.dto.response.GameTimeline;
import com.example.playgroundmanage.dto.response.PlaygroundDto;
import com.example.playgroundmanage.game.repository.CampusRepository;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.repository.PlaygroundRepository;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.util.Util;
import com.example.playgroundmanage.vo.Campus;
import com.example.playgroundmanage.vo.Playground;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaygroundService {

    private final PlaygroundRepository playgroundRepository;

    private final FileHandler fileHandler;

    private final CampusRepository campusRepository;


    @Transactional
    public MultipartFile getPlaygroundImg(Long playgroundId) throws IOException {
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow();
        return fileHandler.extractFile(playground.getImg());
    }

    @Transactional
    public List<PlaygroundDto> getPlaygroundByCampus(Long campusId) {
        Campus campus = campusRepository.findById(campusId).orElseThrow();
        return campus.getPlaygrounds().stream()
                .map(p -> PlaygroundDto.builder().playgroundId(p.getId())
                        .build()).toList();
    }

    public List<GameThumbnail> getGamesThumbnailOrderedByLatest(Long playgroundId) {
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow();
        List<Game> notStartedGames =  playground.orderedByLatest(playground.getGames());
        return notStartedGames.stream()
                .map(g -> GameThumbnail.builder()
                        .hostName(g.getHost().getNickname())
                        .gameStart(Util.localDateToYearMonthDateTimeString(g.getGameStartDateTime()))
                        .time(g.getRunningTime())
                        .sportsEvent(g.getSportsEvent())
                        .build())
                .toList();
    }

    @Transactional
    public GameThumbnail getGameInProgress(Long playgroundId, LocalDateTime currentTime) {
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow();
        Game game = playground.getGames().stream()
                .filter(g -> g.gameOnGoing(currentTime))
                .findFirst().orElseThrow();

        return GameThumbnail.builder()
                .gameId(game.getId())
                .gameStart(Util.localDateToYearMonthDateTimeString(game.getGameStartDateTime()))
                .time(game.getRunningTime())
                .hostName(game.getHost().getNickname())
                .build();
    }

    @Transactional
    public List<GameTimeline> getDailyGameTimelines(Long playgroundId, LocalDateTime day) {
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow();
        List<Game> dailyGames = getDailyGames(playground.getGames(), day);
        return dailyGames.stream().map(g -> GameTimeline.builder()
                .start(g.getGameStartDateTime())
                .end(g.getGameStartDateTime().plusMinutes(g.getRunningTime()))
                .build()).toList();
    }

    private List<Game> getDailyGames(List<Game> games, LocalDateTime day) {
        return games.stream()
                .filter(g -> g.isDayGame(day))
                .toList();
    }

    @Transactional
    public boolean isExistGameTime(Long playgroundId, LocalDateTime day, Long runningTime) {
        //TODO 모든 게임을 불러오기에는 너무 많으니 그 날과 그 다음날까지만 불러오기.
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow();
        List<Game> games = playground.getGames();
        return games.stream()
                .filter(g -> g.isTimeRangeOverlapping(day, runningTime))
                .toList().size() != 0;
    }

}
