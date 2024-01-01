package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.JoinGameRequestDto;
import com.example.playgroundmanage.dto.SubTeamRegistrationParams;
import com.example.playgroundmanage.dto.SubTeamRequest;
import com.example.playgroundmanage.dto.reqeust.UserJoinGameParams;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.game.service.RequestServiceFinder;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RequestController {

    private final RequestServiceFinder requestServiceFinder;

    @PostMapping("/game/{gameId}/join/{request-type}")
    public void generateJoinGameRequest(@RequestBody UserJoinGameParams userJoinGameParams, @AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long gameId, @PathVariable("request-type") String type) {
        RequestService requestService = requestServiceFinder.find(type);

        JoinGameRequestDto joinGameRequestDto = userJoinGameParams.toJoinGameRequestDto(userDetails.getUser());

        requestService.generateRequest(gameId, joinGameRequestDto);
    }

    @PostMapping("/game/{gameId}/request/create-team")
    public void createSubTeam(@RequestBody SubTeamRequest subTeamRequest, @AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long gameId) {
        SubTeamRegistrationParams subTeamRegistrationParams = SubTeamRegistrationParams.builder()
                .gameId(gameId)
                .teamId(subTeamRequest.getTeamId())
                .user(userDetails.getUser())
                .build();
        //soloJoinGameRequestService.createSubTeamRequest(subTeamRegistrationParams);
    }
}
