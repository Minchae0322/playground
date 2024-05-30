package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.althlectis.service.AthleticsService;
import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.dto.reqeust.GameRegistration;
import com.example.playgroundmanage.game.dto.*;
import com.example.playgroundmanage.game.service.GameGenerator;
import com.example.playgroundmanage.game.GameGeneratorFactory;
import com.example.playgroundmanage.game.GameResultManagerFactory;
import com.example.playgroundmanage.game.GameResultManger;
import com.example.playgroundmanage.game.service.GameService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import com.example.playgroundmanage.location.service.PlaygroundService;
import com.example.playgroundmanage.althlectis.service.factory.AthleticsGeneratorFactory;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    private final AthleticsService athleticsService;

    private final PlaygroundService playgroundService;

    private final GameGeneratorFactory gameGeneratorFactory;

    private final GameResultManagerFactory gameResultManagerFactory;

    private final AthleticsGeneratorFactory athleticsGeneratorFactory;


    @GetMapping("/game/{gameId}/{gameType}/{gameTeamSide}")
    public GameTeamResponseDto getGameTeam(@PathVariable Long gameId, @PathVariable String gameType, @PathVariable String gameTeamSide) {
        GameGenerator gameGenerator = gameGeneratorFactory.find(gameType);

        GameTeamRequestDto gameTeamRequestDto = GameTeamRequestDto.builder()
                .gameId(gameId)
                .gameTeamSide(GameTeamSide.fromString(gameTeamSide))
                .build();

        return gameGenerator.getGameTeamInfos(gameTeamRequestDto);
    }

   /* @GetMapping("/game/{gameId}/info")
    public GameResponse getGameInfo(@PathVariable Long gameId) {
        return athleticsService.getGameInfo(gameId);
    }*/

    @GetMapping("/game/{gameId}/info")
    public GameResponseDto getGameInfo(@PathVariable Long gameId) {
        return gameService.getGameInfo(gameId);
    }

   /* @PostMapping("/game/generate")
    public ResponseEntity<Long> generateGame(@RequestBody @Valid GameGenerationRequest gameRegistration, @AuthenticationPrincipal MyUserDetails userDetails) {
        playgroundService.isValidGameStartTime(gameRegistration.playgroundId(), gameRegistration.toGameTimeDto());
        AthleticsGenerator gameGenerator = athleticsGeneratorFactory.find(gameRegistration.gameType());

        return ResponseEntity.ok(gameGenerator.generate(userDetails.getUser().getId(), gameRegistration));
    }*/

    @PostMapping("/game/generate")
    public ResponseEntity<Long> generateGame(@RequestBody GameRegistration gameRegistration, @AuthenticationPrincipal MyUserDetails userDetails) {
        GameDto gameDto = GameDto.builder()
                .startDateTime(MyDateTime.initMyDateTime(gameRegistration.getGameStartDateTime()))
                .host(userDetails.getUser())
                .playgroundId(gameRegistration.getPlaygroundId())
                .gameType(GameType.fromString(gameRegistration.getGameType()))
                .gameName(gameRegistration.getGameName())
                .runningTime(gameRegistration.getRunningTime())
                .sportsEvent(SportsEvent.fromString(gameRegistration.getSportsEvent()))
                .build();

        GameTimeDto gameTimeDto = GameTimeDto.builder()
                .runningTime(gameDto.getRunningTime())
                .startDateTime(gameDto.getStartDateTime().getLocalDateTime())
                .build();

        playgroundService.isValidGameStartTime(gameDto.getPlaygroundId(), gameTimeDto);
        GameGenerator gameGenerator = gameGeneratorFactory.find(gameRegistration.getGameType());

        return ResponseEntity.ok(gameGenerator.generate(gameDto));
    }




    @PreAuthorize("hasPermission(#gameId,'summit_game','CREATE')")
    @PostMapping("/game/result/{gameId}/{gameType}")
    public ResponseEntity<String> summitGameResult(@RequestBody GameResultDto.GameResultRequestDto gameResultRequestDto, @PathVariable String gameType,
                                 @PathVariable Long gameId) {
        GameResultManger gameResultManger = gameResultManagerFactory.find(gameType);

        gameResultManger.submitGameResult(gameResultRequestDto);

        return ResponseEntity.ok("success");
    }

    @PreAuthorize("hasPermission(#gameId,'delete_game','DELETE')")
    @DeleteMapping("/user/game/{gameId}/delete")
    public void deleteGame(@AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Long gameId) {
        gameService.deleteGame(gameId);
    }

}
