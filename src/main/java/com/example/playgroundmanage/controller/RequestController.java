package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.GameRequestDto;
import com.example.playgroundmanage.dto.GameRequestInfoDto;
import com.example.playgroundmanage.dto.reqeust.UserJoinGameParams;
import com.example.playgroundmanage.dto.response.PendingGameRequest;
import com.example.playgroundmanage.game.service.GameManagementService;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.game.service.RequestServiceFinder;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RequestController {

    private final RequestServiceFinder requestServiceFinder;

    private final GameManagementService gameManagementService;

    @PostMapping("/game/{gameId}/join/{requestType}")
    public void generateJoinGameRequest(@RequestBody UserJoinGameParams userJoinGameParams, @AuthenticationPrincipal MyUserDetails userDetails, @PathVariable Long gameId, @PathVariable("requestType") String type) {
        RequestService requestService = requestServiceFinder.find(type);

        GameRequestDto gameRequestDto = userJoinGameParams.toJoinGameRequestDto(userDetails.getUser());

        requestService.generateRequest(gameId, gameRequestDto);
    }

    @PreAuthorize("hasPermission(#requestId,'requestAccept','UPDATE')")
    @PatchMapping("/game/accept/{requestId}/{requestType}")
    public void acceptGameRequest(@PathVariable Long requestId, @PathVariable("requestType") String type) {
        RequestService requestService = requestServiceFinder.find(type);

        requestService.acceptRequest(requestId);
    }

    @DeleteMapping("/game/reject/{requestId}/{requestType}")
    public void declineGameRequest(@PathVariable Long requestId, @PathVariable("requestType") String type) {
        RequestService requestService = requestServiceFinder.find(type);

        requestService.declineRequest(requestId);
    }

    @GetMapping("/user/pending/request")
    public List<PendingGameRequest> getPendingGameRequests(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        List<GameRequestInfoDto> pendingRequest = gameManagementService.getPendingGameRequests(myUserDetails.getUser());

        return pendingRequest.stream()
                .map(GameRequestInfoDto::toPendingGameRequest)
                .toList();
    }

}
