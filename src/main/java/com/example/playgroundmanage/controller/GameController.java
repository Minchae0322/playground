package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.SubTeamRegistrationParams;
import com.example.playgroundmanage.dto.UserJoinTeamParams;
import com.example.playgroundmanage.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/join")
    public void userJoinTeam() {
        UserJoinTeamParams userJoinTeamParams = UserJoinTeamParams.builder()
                .build();
        gameService.joinUserInSubTeam(userJoinTeamParams);
    }

    @GetMapping("/game/create/subTeam")
    public void createSubTeam() {
        SubTeamRegistrationParams subTeamRegistrationParams = SubTeamRegistrationParams.builder()
                .build();
        Long subTeamId = gameService.generateSubTeam(subTeamRegistrationParams);
        UserJoinTeamParams userJoinTeamParams =  UserJoinTeamParams.builder()
                .subTeamId(subTeamId)
                .build();
        gameService.joinUserInSubTeam(userJoinTeamParams);
    }
}
