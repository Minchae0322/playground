package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.dto.GameTimeInfo;
import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.dto.reqeust.GameRegistration;
import com.example.playgroundmanage.dto.response.SubTeamDto;
import com.example.playgroundmanage.game.service.GameService;
import com.example.playgroundmanage.game.service.SubTeamService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import com.example.playgroundmanage.service.PlaygroundService;
import com.example.playgroundmanage.type.MatchTeamSide;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;


    private final SubTeamService subTeamService;

    private final PlaygroundService playgroundService;


    @GetMapping("/game/{gameId}/homeTeams")
    public List<SubTeamDto> getHomeTeams(@PathVariable Long gameId) {
        return gameService.getTeamsBySide(gameId, MatchTeamSide.HOME);
    }

    @GetMapping("/game/{gameId}/awayTeams")
    public List<SubTeamDto> getAwayTeams(@PathVariable Long gameId) {
        return gameService.getTeamsBySide(gameId, MatchTeamSide.AWAY);
    }

    @PostMapping("/game/generate")
    public ResponseEntity<Long> generateGame(@RequestBody GameRegistration gameRegistration, @AuthenticationPrincipal MyUserDetails userDetails) {
        GameDto gameDto = GameDto.builder()
                .startDateTime(MyDateTime.initMyDateTime(gameRegistration.getGameStartDateTime()))
                .host(userDetails.getUser())
                .gameName(gameRegistration.getGameName())
                .runningTime(gameRegistration.getRunningTime())
                .sportsEvent(gameRegistration.getSportsEvent())
                .build();

        Long generatedGameId = gameService.generateGame(gameRegistration.getPlaygroundId(), gameDto);
        subTeamService.generateSoloSubTeamBothCompetingTeam(generatedGameId);

        return ResponseEntity.ok(generatedGameId);
    }


}
