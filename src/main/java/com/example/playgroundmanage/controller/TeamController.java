package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.TeamRequestDto;
import com.example.playgroundmanage.dto.TeamRegistrationRequest;
import com.example.playgroundmanage.dto.TeamJoinRequestDto;
import com.example.playgroundmanage.dto.response.TeamInfoResponse;
import com.example.playgroundmanage.dto.response.TeamMemberDto;
import com.example.playgroundmanage.dto.response.TeamResponseDto;
import com.example.playgroundmanage.team.service.TeamService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import com.example.playgroundmanage.type.SportsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/team/{teamId}/check/member")
    public boolean isTeamMember(@PathVariable Long teamId, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        return teamService.isTeamMember(teamId, myUserDetails.getUser());
    }

    @PreAuthorize("hasPermission(#teamId,'delete_team','DELETE')")
    @DeleteMapping("/user/team/{teamId}/delete")
    public void deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeam(teamId);
    }


    @PostMapping(value = "/team/build", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public TeamInfoResponse generateTeam(@AuthenticationPrincipal MyUserDetails myUserDetails, @RequestPart("team") TeamRegistrationRequest params, @RequestPart(value = "imageFile", required = false) MultipartFile multipartFile) throws IOException {
        TeamRequestDto teamRequestDto = TeamRequestDto.builder()
                .teamPic(multipartFile)
                .teamDescription(params.getTeamDescription())
                .teamName(params.getTeamName())
                .leader(myUserDetails.getUser())
                .sportsEvent(SportsEvent.fromString(params.getSportsEvent()))
                .build();

        return TeamInfoResponse.builder()
                .teamId(teamService.generateTeam(teamRequestDto))
                .build();
    }

    @PostMapping("/team/name/validate")
    public void validate(@RequestBody TeamRegistrationRequest teamRegistrationRequest) {
        teamService.validateTeamName(teamRegistrationRequest.getTeamName());
    }

    @GetMapping("/team/{teamId}/members")
    public List<TeamMemberDto> getTeamMembers(@PathVariable Long teamId) {
        return teamService.getTeamMembers(teamId);
    }

    @PostMapping("/team/list/{type}")
    public List<TeamResponseDto> getTeams(@RequestBody TeamJoinRequestDto teamJoinRequestDto, @PathVariable String type) {
        return teamService.getTeams(teamJoinRequestDto, type);
    }

}
