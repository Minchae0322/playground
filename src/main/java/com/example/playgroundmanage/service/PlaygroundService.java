package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.dto.response.GameTimeline;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.repository.PlaygroundRepository;
import com.example.playgroundmanage.vo.Playground;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaygroundService {

    private final PlaygroundRepository playgroundRepository;

    public List<GameThumbnail> getGamesThumbnailOrderedByLatest(Long playgroundId) {
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow();
        List<Game> notStartedGames =  playground.orderedByLatest(playground.getGames());
        return notStartedGames.stream()
                .map(g -> GameThumbnail.builder()
                        .hostName(g.getHost().getNickname())
                        .gameStart(g.getGameStartDateTime())
                        .time(g.getRunningTime())
                        .sportsEvent(g.getSportsEvent())
                        .build())
                .toList();
    }

    @Transactional
    public Game getGameInProgress(Long playgroundId, LocalDateTime currentTime) {
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow();

        return playground.getGames().stream()
                .filter(g -> g.gameOnGoing(currentTime))
                .findFirst().orElseThrow();
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
