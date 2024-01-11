package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.GameRequestDto;
import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.dto.TeamRequestDto;
import com.example.playgroundmanage.dto.reqeust.PendingRequestParams;
import com.example.playgroundmanage.dto.reqeust.UserJoinGameParams;
import com.example.playgroundmanage.dto.reqeust.UserJoinTeamParams;
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
        gameRequestDto.setGameId(gameId);

        requestService.generateRequest(gameRequestDto);
    }

    @PreAuthorize("hasPermission(#requestId,'requestAccept_game','UPDATE')")
    @PatchMapping("/game/accept/{requestId}/{requestType}")
    public void acceptGameRequest(@PathVariable Long requestId, @PathVariable("requestType") String type) {
        RequestService requestService = requestServiceFinder.find(type);

        requestService.acceptRequest(requestId);
    }

    @PreAuthorize("hasPermission(#requestId,'requestAccept_game','DELETE')")
    @DeleteMapping("/game/reject/{requestId}/{requestType}")
    public void declineGameRequest(@PathVariable Long requestId, @PathVariable("requestType") String type) {
        RequestService requestService = requestServiceFinder.find(type);

        requestService.declineRequest(requestId);
    }

    @GetMapping("/user/pending/request")
    public List<PendingGameRequest> getPendingGameRequests(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        List<RequestInfoDto> pendingRequest = gameManagementService.getPendingGameRequests(myUserDetails.getUser());

        return pendingRequest.stream()
                .map(RequestInfoDto::toPendingGameRequest)
                .toList();
    }

    @PostMapping("/user/pending/request/{requestType}")
    public List<PendingGameRequest> getPendingRequests(@RequestBody PendingRequestParams pendingRequestParams, @AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable String requestType) {

        RequestService requestService = requestServiceFinder.find(requestType);

        pendingRequestParams.setHost(myUserDetails.getUser());
        List<RequestInfoDto> pendingRequest = requestService.getPendingRequests(pendingRequestParams);

        return pendingRequest.stream()
                .map(RequestInfoDto::toPendingGameRequest)
                .toList();
    }

    @PostMapping("/team/join/{requestType}")
    public void createTeamJoinRequest(@RequestBody UserJoinTeamParams userJoinTeamParams, @PathVariable("requestType") String type,
                                      @AuthenticationPrincipal MyUserDetails myUserDetails) {
        RequestService requestService = requestServiceFinder.find(type);

        TeamRequestDto teamRequestDto = userJoinTeamParams.toTeamRequestDto(myUserDetails.getUser());

        requestService.generateRequest(teamRequestDto);
    }

    @PreAuthorize("hasPermission(#requestId,'requestAccept_team','UPDATE')")
    @PatchMapping("/team/accept/{requestId}/{requestType}")
    public void acceptTeamRequest(@PathVariable Long requestId, @PathVariable("requestType") String type) {
        RequestService requestService = requestServiceFinder.find(type);

        requestService.acceptRequest(requestId);
    }
    @PreAuthorize("hasPermission(#requestId,'requestAccept_team','UPDATE')")
    @PatchMapping("/team/rejuect/{requestId}/{requestType}")
    public void rejectTeamRequest(@PathVariable Long requestId, @PathVariable("requestType") String type) {
        RequestService requestService = requestServiceFinder.find(type);

        requestService.acceptRequest(requestId);
    }
}
