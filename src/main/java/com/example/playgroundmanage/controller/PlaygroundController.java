package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.game.dto.GameDto;
import com.example.playgroundmanage.dto.reqeust.GameTimeInfo;
import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.game.dto.GameResponseDto;
import com.example.playgroundmanage.game.dto.GameTimeDto;
import com.example.playgroundmanage.dto.response.OccupiedTime;
import com.example.playgroundmanage.location.dto.PlaygroundResponseDto;
import com.example.playgroundmanage.login.service.UserService;
import com.example.playgroundmanage.location.service.PlaygroundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaygroundController {

    private final PlaygroundService playgroundService;

    @GetMapping("/playground/{playgroundId}/current")
    public ResponseEntity<GameResponseDto> getOngoingGame(@PathVariable Long playgroundId) {
        return ResponseEntity.ok(playgroundService.getOngoingGame(playgroundId));
    }

    @GetMapping("/playground/{playgroundId}/upComing")
    public List<GameResponseDto> getUpcomingGames(@PathVariable Long playgroundId) {
        return playgroundService.getUpcomingGames(playgroundId);
    }

    @GetMapping("/playground/{playgroundId}/info")
    public PlaygroundResponseDto getPlaygroundInfo(@PathVariable Long playgroundId) {
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
