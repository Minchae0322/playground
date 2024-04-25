package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.game.dto.GameDto;
import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.game.dto.GameResponseDto;
import com.example.playgroundmanage.location.dto.CampusResponseDto;
import com.example.playgroundmanage.location.dto.PlaygroundResponseDto;
import com.example.playgroundmanage.location.service.CampusService;
import com.example.playgroundmanage.location.service.PlaygroundService;
import com.example.playgroundmanage.location.service.SchoolService;
import com.example.playgroundmanage.type.SportsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    private final CampusService campusService;


    @GetMapping("/school/{schoolId}/campus/info")
    public List<CampusResponseDto> getCampusInfos(@PathVariable Long schoolId) {
        return schoolService.getCampusInfo(schoolId);
    }

    @GetMapping("/school/{schoolId}/playground/{sportsType}")
    public List<PlaygroundResponseDto> getPlaygroundsBySportsType(@PathVariable String sportsType, @PathVariable Long schoolId) {
        return schoolService.getPlaygroundBySchoolAndSportsType(schoolId, SportsEvent.valueOf(sportsType));
    }

    @GetMapping("/school/{schoolId}/upcoming/{sportsType}")
    public List<GameResponseDto> getUpcomingGamesBySportsEvent(@PathVariable String sportsType, @PathVariable Long schoolId) {

        return campusService.getUpcomingGamesBySportsEvent(schoolId, SportsEvent.valueOf(sportsType));
    }

    @GetMapping("/school/{schoolId}/upcoming")
    public List<GameThumbnail> getUpcomingGamesBySchool( @PathVariable Long schoolId) {
        List<GameDto> upcomingGames = schoolService.getUpcomingGamesBySchool(schoolId);

        return upcomingGames.stream()
                .map(GameDto::toGameThumbnail)
                .toList();
    }

}
