package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.GameDateDto;
import com.example.playgroundmanage.dto.SubTeamRegistrationParams;
import com.example.playgroundmanage.dto.SubTeamRequest;
import com.example.playgroundmanage.dto.response.GameTimeline;
import com.example.playgroundmanage.game.service.GameService;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestController {
    private final GameService gameService;

    private final RequestService requestService;

    @PostMapping("/game/{gameId}/request/solo")
    public void userJoinSoloTeam(@RequestBody SubTeamRequest subTeamRequest, @AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long gameId) {
        SubTeamRegistrationParams subTeamRegistrationParams = SubTeamRegistrationParams.builder()
                .gameId(gameId)
                .teamId(subTeamRequest.getTeamId())
                .user(userDetails.getUser())
                .build();
        requestService.createSoloJoinRequest(subTeamRegistrationParams);
    }

    @PostMapping("/game/{gameId}/request/create-team")
    public void createSubTeam(@RequestBody SubTeamRequest subTeamRequest, @AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long gameId) {
        SubTeamRegistrationParams subTeamRegistrationParams = SubTeamRegistrationParams.builder()
                .gameId(gameId)
                .teamId(subTeamRequest.getTeamId())
                .user(userDetails.getUser())
                .build();
        requestService.createSubTeamRequest(subTeamRegistrationParams);
    }
}
