package com.example.playgroundmanage.request.api;

import com.example.playgroundmanage.request.dto.AthleticsJoinRequest;
import com.example.playgroundmanage.request.dto.PendingRequestResponse;
import com.example.playgroundmanage.request.dto.TeamJoinRequestDto;
import com.example.playgroundmanage.request.service.AthleticsRequestService;
import com.example.playgroundmanage.request.RequestServiceFinder;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import com.example.playgroundmanage.request.service.TeamRequestService;
import com.example.playgroundmanage.request.service.impl.FriendlyAthleticsJoinAthleticsRequestService;
import com.example.playgroundmanage.request.service.impl.RankAthleticsJoinRequestService;
import com.example.playgroundmanage.request.service.impl.TeamJoinRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class RequestController {

    private final TeamJoinRequestService teamJoinRequestService;

    private final RequestServiceFinder requestServiceFinder;

    private final RankAthleticsJoinRequestService rankAthleticsJoinRequestService;

    private final FriendlyAthleticsJoinAthleticsRequestService friendlyAthleticsJoinAthleticsRequestService;

    @PostMapping("/game/join/{requestType}")
    public void generateJoinGameRequest(@RequestBody @Validated AthleticsJoinRequest athleticsJoinRequest,
                                        @AuthenticationPrincipal MyUserDetails userDetails,
                                        @PathVariable("requestType") String type) {
        AthleticsRequestService requestService = requestServiceFinder.find(type);

        requestService.generateRequest(userDetails.getUser().getId(), athleticsJoinRequest);
    }

    @PostMapping("/team/join/{requestType}")
    public void generateTeamJoinRequest(@RequestBody @Validated TeamJoinRequestDto teamJoinRequestDto,
                                        @PathVariable("requestType") String type,
                                        @AuthenticationPrincipal MyUserDetails myUserDetails) {
        teamJoinRequestService.generateRequest(myUserDetails.getUser().getId(), teamJoinRequestDto);
    }

    @GetMapping("/user/pending/request/game")
    public List<PendingRequestResponse> getAllPendingGameRequests(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        return Stream.concat(
                friendlyAthleticsJoinAthleticsRequestService.getPendingRequests(myUserDetails.getUser().getId()).stream(),
                rankAthleticsJoinRequestService.getPendingRequests(myUserDetails.getUser().getId()).stream()
        ).collect(Collectors.toList());
    }

    @PatchMapping("/game/accept/{requestId}/{requestType}")
    public void acceptGameRequest(@PathVariable Long requestId, @PathVariable("requestType") String type) {
        AthleticsRequestService requestService = requestServiceFinder.find(type);

        requestService.acceptRequest(requestId);
    }


    @PreAuthorize("hasPermission(#requestId,'requestAccept_game','DELETE')")
    @DeleteMapping("/game/reject/{requestId}/{requestType}")
    public void declineGameRequest(@PathVariable Long requestId, @PathVariable("requestType") String type) {
        AthleticsRequestService requestService = requestServiceFinder.find(type);

        requestService.rejectRequest(requestId);
    }

    /*
    @PostMapping("/user/pending/request/team/{requestType}")
    public List<PendingTeamRequest> getPendingTeamRequests(@RequestBody PendingRequestParams pendingRequestParams, @AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable String requestType) {

        RequestService requestService = requestServiceFinder.find(requestType);

        pendingRequestParams.setHost(myUserDetails.getUser());
        List<RequestInfoDto> pendingRequest = requestService.getPendingRequests(pendingRequestParams);

        return pendingRequest.stream()
                .map(RequestInfoDto::toPendingTeamRequest)
                .toList();
    }
    @PostMapping("/team/join/{requestType}")
    public void createTeamJoinRequest(@RequestBody UserJoinTeamParams userJoinTeamParams, @PathVariable("requestType") String type,
                                      @AuthenticationPrincipal MyUserDetails myUserDetails) {
        RequestService requestService = requestServiceFinder.find(type);

        TeamJoinRequestDto teamJoinRequestDto = userJoinTeamParams.toTeamRequestDto(myUserDetails.getUser());

        requestService.generateRequest(teamJoinRequestDto);
    }

    @PreAuthorize("hasPermission(#requestId,'requestAccept_team','UPDATE')")
    @PatchMapping("/team/accept/{requestId}/{requestType}")
    public void acceptTeamRequest(@PathVariable Long requestId, @PathVariable("requestType") String type) {
        RequestService requestService = requestServiceFinder.find(type);

        requestService.acceptRequest(requestId);
    }
    @PreAuthorize("hasPermission(#requestId,'requestAccept_team','UPDATE')")
    @PatchMapping("/team/reject/{requestId}/{requestType}")
    public void rejectTeamRequest(@PathVariable Long requestId, @PathVariable("requestType") String type) {
        RequestService requestService = requestServiceFinder.find(type);

        requestService.declineRequest(requestId);
    }*/
}
