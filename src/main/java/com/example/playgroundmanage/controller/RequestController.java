package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.JoinGameRequestDto;
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

    @PostMapping("/game/{gameId}/join/{requestType}")
    public void generateJoinGameRequest(@RequestBody UserJoinGameParams userJoinGameParams, @AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long gameId, @PathVariable("requestType") String type) {
        RequestService requestService = requestServiceFinder.find(type);

        JoinGameRequestDto joinGameRequestDto = userJoinGameParams.toJoinGameRequestDto(userDetails.getUser());

        requestService.generateRequest(gameId, joinGameRequestDto);
    }

    @GetMapping("/game/accept/{requestId}/{requestType}")
    public void acceptGameRequest(@PathVariable Long requestId, @PathVariable("requestType") String type) {
        RequestService requestService = requestServiceFinder.find(type);

        requestService.acceptRequest(requestId);
    }

    @DeleteMapping("/game/accept/{requestId}/{requestType}")
    public void declineGameRequest(@PathVariable Long requestId, @PathVariable("requestType") String type) {
        RequestService requestService = requestServiceFinder.find(type);

        requestService.declineRequest(requestId);
    }

}
