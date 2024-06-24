package com.example.playgroundmanage.location.api;

import com.example.playgroundmanage.althlectis.dto.response.AthleticsResponse;
import com.example.playgroundmanage.location.dto.CampusResponseDto;
import com.example.playgroundmanage.location.dto.response.PlaygroundInfoResponse;
import com.example.playgroundmanage.location.service.CampusService;
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
    public List<PlaygroundInfoResponse> getPlaygroundsByCampusAndSportsType(@PathVariable Long campusId, @PathVariable String sportsType) {
        return campusService.getPlaygroundByCampusAndSportsType(campusId, SportsEvent.valueOf(sportsType));
    }

    @GetMapping("/campus/{campusId}/upcoming/{sportsType}")
    public List<AthleticsResponse> getUpcomingGamesInCampusBySportsEvent(@PathVariable Long campusId, @PathVariable String sportsType) {
        return campusService.getUpcomingGamesInCampusBySportsEvent(campusId, SportsEvent.valueOf(sportsType));
    }

    @GetMapping("/campus/list/{schoolId}")
    public List<CampusResponseDto> getCampusList(@PathVariable Long schoolId) {
        return campusService.getCampusList(schoolId);
    }
}
