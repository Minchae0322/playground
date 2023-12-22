package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.dto.response.PlaygroundInfoDto;
import com.example.playgroundmanage.service.PlaygroundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaygroundController {

    private final PlaygroundService playgroundService;

    @GetMapping("/playground/{playgroundId}/current")
    public GameThumbnail getActiveGameInfo(@PathVariable Long playgroundId) {
        return playgroundService.getGameInProgress(playgroundId, LocalDateTime.now());
    }

    @GetMapping("/playground/{playgroundId}/upComing")
    public List<GameThumbnail> getThreeGameOrderedByStartTime(@PathVariable Long playgroundId) {
        return playgroundService.getTopThreeUpcomingGames(playgroundId);
    }

    @GetMapping("/playground/{playgroundId}/info")
    public PlaygroundInfoDto getPlaygroundInfo(@PathVariable Long playgroundId) {
        return playgroundService.getPlaygroundInfo(playgroundId);
    }
}
