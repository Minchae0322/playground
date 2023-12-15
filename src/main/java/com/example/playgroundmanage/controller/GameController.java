package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.GameDateDto;
import com.example.playgroundmanage.dto.SubTeamRegistrationParams;
import com.example.playgroundmanage.dto.UserJoinTeamParams;
import com.example.playgroundmanage.dto.response.GameTimeline;
import com.example.playgroundmanage.dto.response.SubTeamDto;
import com.example.playgroundmanage.game.service.GameService;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    private final RequestService requestService;

    @PostMapping("/game/{gameId}")
    public List<GameTimeline> userJoinTeam(@PathVariable("gameId") Long gameId, @RequestBody GameDateDto gameDateDto) {
        System.out.println(gameDateDto.getDate());
        return List.of(GameTimeline.builder()
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build());
    }

    @GetMapping("/game/create/subTeam")
    public void createSubTeam(@RequestBody SubTeamRegistrationParams subTeamRegistrationParams, @AuthenticationPrincipal MyUserDetails userDetails) {
        subTeamRegistrationParams.addUser(userDetails.getUser());
        requestService.createGameTeamJoinRequest(subTeamRegistrationParams);
    }

    @GetMapping("/game/{gameId}/homeTeams")
    public List<SubTeamDto> getHomeTeams(@PathVariable Long gameId) {
        return gameService.getHomeTeams(gameId);
    }
}
