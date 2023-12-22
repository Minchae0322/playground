package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.TeamRegistration;
import com.example.playgroundmanage.dto.TeamRegistrationRequest;
import com.example.playgroundmanage.dto.response.TeamInfoResponse;
import com.example.playgroundmanage.game.service.TeamService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/team/{teamId}/info")
    public TeamInfoResponse getTeamInfo(@PathVariable Long teamId) throws IOException {
        return teamService.getTeamInfo(teamId);
    }



    @PostMapping(value = "/team/build", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public TeamInfoResponse generateTeam(@AuthenticationPrincipal MyUserDetails myUserDetails, @RequestPart("team") TeamRegistrationRequest params, @RequestPart(value = "imageFile", required = false) MultipartFile multipartFile) throws IOException {
        TeamRegistration teamRegistration = TeamRegistration.builder()
                .teamPic(multipartFile)
                .teamDescription(params.getTeamDescription())
                .teamName(params.getTeamName())
                .leader(myUserDetails.getUser())
                .sportsEvent(SportsEvent.fromString(params.getSportsEvent()))
                .build();
        return TeamInfoResponse.builder()
                .teamId(teamService.generateTeam(teamRegistration))
                .build();
    }

    @PostMapping("/team/name/validate")
    public void validate(@RequestBody TeamRegistrationRequest teamRegistrationRequest) {
        teamService.validateTeamName(teamRegistrationRequest.getTeamName());
    }


}
