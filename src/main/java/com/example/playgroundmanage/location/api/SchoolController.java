package com.example.playgroundmanage.location.api;

import com.example.playgroundmanage.althlectis.dto.response.AthleticsResponse;
import com.example.playgroundmanage.location.dto.CampusResponseDto;
import com.example.playgroundmanage.location.dto.response.PlaygroundInfoResponse;
import com.example.playgroundmanage.location.service.CampusService;
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
    public List<CampusResponseDto> getCampusList(@PathVariable Long schoolId) {
        return schoolService.getCampusList(schoolId);
    }

    @GetMapping("/school/{schoolId}/playground/{sportsType}")
    public List<PlaygroundInfoResponse> getPlaygroundsBySportsType(@PathVariable String sportsType, @PathVariable Long schoolId) {
        return schoolService.getPlaygroundBySchoolAndSportsType(schoolId, SportsEvent.valueOf(sportsType));
    }

    @GetMapping("/school/{schoolId}/upcoming/{sportsType}")
    public List<AthleticsResponse> getUpcomingGamesBySportsEvent(@PathVariable String sportsType, @PathVariable Long schoolId) {
        return schoolService.getUpcomingGamesBySchoolAndSportsEvent(schoolId, SportsEvent.valueOf(sportsType));
    }

    @GetMapping("/school/{schoolId}/upcoming")
    public List<AthleticsResponse> getUpcomingGamesBySchool( @PathVariable Long schoolId) {
        return schoolService.getUpcomingGamesBySchool(schoolId);


    }

}
