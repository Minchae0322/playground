package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.location.dto.CampusResponseDto;
import com.example.playgroundmanage.location.dto.PlaygroundResponseDto;
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

    private final PlaygroundService playgroundService;

    private final SchoolService schoolService;
    @GetMapping("/school/{schoolId}/campus/info")
    public List<CampusResponseDto> getCampusInfo(@PathVariable Long schoolId) {
        return schoolService.getCampusInfo(schoolId);
    }

    @GetMapping("/school/{schoolId}/playground/{sportsType}")
    public List<PlaygroundResponseDto> getPlaygroundsBySportsType(@PathVariable String sportsType, @PathVariable Long schoolId) {
        return playgroundService.getPlaygroundByCampusAndSportsType(schoolId, SportsEvent.valueOf(sportsType));
    }

}
