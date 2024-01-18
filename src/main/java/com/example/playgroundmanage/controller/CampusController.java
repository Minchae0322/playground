package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.location.dto.PlaygroundResponseDto;
import com.example.playgroundmanage.location.service.CampusService;
import com.example.playgroundmanage.location.service.PlaygroundService;
import com.example.playgroundmanage.type.SportsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CampusController {

    private final PlaygroundService playgroundService;

    private final CampusService campusService;

    @GetMapping("/playground/{campusId}/{sportsType}")
    public List<PlaygroundResponseDto> getPlaygroundsByCampusAndSportsType(@PathVariable Long campusId, @PathVariable String sportsType) {
        return playgroundService.getPlaygroundByCampusAndSportsType(campusId, SportsEvent.valueOf(sportsType));
    }

    @GetMapping("/campus/{campusId}/upcoming/{sportsType}")
    public List<GameThumbnail> getUpcomingGamesInCampusBySportsEvent(@PathVariable Long campusId, @PathVariable String sportsType) {
        List<GameDto> upcomingGames = campusService.getUpcomingGamesInCampusBySportsEvent(campusId, SportsEvent.valueOf(sportsType));

        return upcomingGames.stream()
                .map(GameDto::toGameThumbnail)
                .toList();
    }

}
