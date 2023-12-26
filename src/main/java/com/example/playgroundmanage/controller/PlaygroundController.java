package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.GameDateDto;
import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.dto.response.OccupiedTime;
import com.example.playgroundmanage.dto.response.PlaygroundInfoDto;
import com.example.playgroundmanage.service.PlaygroundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/playground/{playgroundId}/occupiedTime")
    public List<OccupiedTime> getOccupiedTimeLines(@PathVariable Long playgroundId, @RequestBody GameDateDto gameDateDto) {
        return playgroundService.getPlaygroundOccupiedTimeLines(playgroundId, gameDateDto);
    }

    @PostMapping("/playground/{playgroundId}/valid-start")
    public boolean validateGameStartTime(@PathVariable Long playgroundId, @RequestBody GameDateDto gameDateDto) {
        return playgroundService.isValidGameStartTime(playgroundId, gameDateDto);
    }
}
