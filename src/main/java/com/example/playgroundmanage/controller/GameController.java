package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.response.SubTeamDto;
import com.example.playgroundmanage.game.service.GameService;
import com.example.playgroundmanage.type.MatchTeamSide;
import lombok.RequiredArgsConstructor;
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



}
