package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.dto.reqeust.GameTimeInfo;
import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.dto.GameTimeDto;
import com.example.playgroundmanage.dto.response.OccupiedTime;
import com.example.playgroundmanage.dto.PlaygroundDto;
import com.example.playgroundmanage.location.dto.PlaygroundResponseDto;
import com.example.playgroundmanage.login.service.UserService;
import com.example.playgroundmanage.location.service.PlaygroundService;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlaygroundController {

    private final PlaygroundService playgroundService;

    private final UserService userService;


    @GetMapping("/playground/{playgroundId}/current")
    public ResponseEntity<GameThumbnail> getOngoingGame(@PathVariable Long playgroundId) {
        GameDto gameDto =  playgroundService.getOngoingGame(playgroundId);

        return ResponseEntity.ok(gameDto.toGameThumbnail(userService.getUserProfileImg(gameDto.getHost())));
    }



    @GetMapping("/playground/{playgroundId}/upComing")
    public List<GameThumbnail> getUpcomingGames(@PathVariable Long playgroundId) {
        List<GameDto> upcomingThreeGames = playgroundService.getUpcomingGames(playgroundId);

        return upcomingThreeGames.stream()
                .map(gameDto -> gameDto.toGameThumbnail(userService.getUserProfileImg(gameDto.getHost())))
                .toList();
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
