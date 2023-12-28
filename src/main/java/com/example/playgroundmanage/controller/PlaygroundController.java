package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.dto.GameTimeInfo;
import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.dto.response.GameTimeDto;
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
        GameDto gameDto =  playgroundService.getGameInProgress(playgroundId);

        return gameDto.toGameThumbnail();
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
    public List<OccupiedTime> getOccupiedTimeLines(@PathVariable Long playgroundId, @RequestBody GameTimeInfo gameTimeInfo) {
        GameTimeDto gameTimeDto = gameTimeInfo.toGameTimeDto();

        return playgroundService.getPlaygroundOccupiedTimeLines(playgroundId, gameTimeDto);
    }

    @PostMapping("/playground/{playgroundId}/valid-start")
    public boolean validateGameStartTime(@PathVariable Long playgroundId, @RequestBody GameTimeInfo gameTimeInfo) {
        GameTimeDto gameTimeDto = gameTimeInfo.toGameTimeDto();

        return playgroundService.isValidGameStartTime(playgroundId, gameTimeDto);
    }

}
