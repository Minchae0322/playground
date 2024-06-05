package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.game.dto.GameDto;
import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.game.dto.GameResponseDto;
import com.example.playgroundmanage.location.dto.AthleticsThumbnailResponse;
import com.example.playgroundmanage.location.dto.CampusResponseDto;
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

    private final CampusService campusService;

    @GetMapping("/playground/{campusId}/{sportsType}")
    public List<PlaygroundResponseDto> getPlaygroundsByCampusAndSportsType(@PathVariable Long campusId, @PathVariable String sportsType) {
        return campusService.getPlaygroundByCampusAndSportsType(campusId, SportsEvent.valueOf(sportsType));
    }

    @GetMapping("/campus/{campusId}/upcoming/{sportsType}")
    public List<AthleticsThumbnailResponse> getUpcomingGamesInCampusBySportsEvent(@PathVariable Long campusId, @PathVariable String sportsType) {
        return campusService.getUpcomingGamesInCampusBySportsEvent(campusId, SportsEvent.valueOf(sportsType));
    }

    @GetMapping("/campus/list/{schoolId}")
    public List<CampusResponseDto> getCampusList(@PathVariable Long schoolId) {
        return campusService.getCampusList(schoolId);
    }
}
