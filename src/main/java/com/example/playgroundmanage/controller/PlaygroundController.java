package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.service.PlaygroundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class PlaygroundController {

    private final PlaygroundService playgroundService;

    @GetMapping("/playground/{playgroundId}/current")
    public void getActiveGameInfo(@PathVariable Long playgroundId) {
        playgroundService.getGameInProgress(playgroundId, LocalDateTime.now());
    }
}
