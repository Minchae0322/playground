package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.dto.GameDateDto;
import com.example.playgroundmanage.dto.SubTeamRegistrationParams;
import com.example.playgroundmanage.dto.UserJoinTeamParams;
import com.example.playgroundmanage.dto.response.GameTimeline;
import com.example.playgroundmanage.dto.response.SubTeamDto;
import com.example.playgroundmanage.game.service.GameService;
import com.example.playgroundmanage.game.service.RequestService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import com.example.playgroundmanage.type.MatchTeamSide;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;


    @GetMapping("/game/{gameId}/homeTeams")
    public List<SubTeamDto> getHomeTeams(@PathVariable Long gameId) throws IOException {
        return gameService.getTeamsBySide(gameId, MatchTeamSide.HOME);
    }

    @GetMapping("/game/{gameId}/awayTeams")
    public List<SubTeamDto> getAwayTeams(@PathVariable Long gameId) throws IOException {
        return gameService.getTeamsBySide(gameId, MatchTeamSide.AWAY);
    }
}
