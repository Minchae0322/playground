package com.example.playgroundmanage.service;

import com.example.playgroundmanage.dto.response.GameThumbnail;
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
    public Game getGameInProgress(Long playgroundId) {
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow();
        LocalDateTime currentTime = LocalDateTime.now();

        return playground.getGames().stream()
                .filter(g -> g.gameOnGoing(currentTime))
                .findFirst().orElseThrow();
    }

}