package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.TeamRegistration;
import com.example.playgroundmanage.dto.TeamRegistrationRequest;
import com.example.playgroundmanage.game.service.TeamService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
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
    public void getTeamInfo(@PathVariable Long teamId) {

    }

    @PostMapping(value = "/team/build", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public void generateTeam(@AuthenticationPrincipal MyUserDetails myUserDetails, @RequestPart("team") TeamRegistrationRequest params, @RequestPart(value = "imageFile", required = false) MultipartFile multipartFile) throws IOException {
        TeamRegistration teamRegistration = TeamRegistration.builder()
                .teamPic(multipartFile)
                .teamDescription(params.getTeamDescription())
                .teamName(params.getTeamName())
                .leader(myUserDetails.getUser())
                .build();
        teamService.generateTeam(teamRegistration);
    }
}
