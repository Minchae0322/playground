package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.dto.GameRegistration;
import com.example.playgroundmanage.dto.response.SubTeamDto;
import com.example.playgroundmanage.game.service.GameService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import com.example.playgroundmanage.type.MatchTeamSide;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;


    @GetMapping("/game/{gameId}/homeTeams")
    public List<SubTeamDto> getHomeTeams(@PathVariable Long gameId) {
        return gameService.getTeamsBySide(gameId, MatchTeamSide.HOME);
    }

    @GetMapping("/game/{gameId}/awayTeams")
    public List<SubTeamDto> getAwayTeams(@PathVariable Long gameId) {
        return gameService.getTeamsBySide(gameId, MatchTeamSide.AWAY);
    }

    @PostMapping("/game/generate")
    public Long generateGame(@RequestBody GameDto gameDto, @AuthenticationPrincipal MyUserDetails userDetails) {
        GameRegistration gameRegistration = GameRegistration.builder()
                .myDateTime(MyDateTime.getMyDateTime(gameDto.getGameStartDateTime()))
                .host(userDetails.getUser())
                .gameName(gameDto.getGameName())
                .runningTime(gameDto.getRunningTime())
                .sportsEvent(gameDto.getSportsEvent())
                .build();

        return gameService.createGame(gameRegistration);
    }


}
