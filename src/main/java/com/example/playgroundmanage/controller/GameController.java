package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.date.MyDateTimeLocal;
import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.dto.UsersGameDto;
import com.example.playgroundmanage.dto.reqeust.GameRegistration;
import com.example.playgroundmanage.dto.SubTeamDto;
import com.example.playgroundmanage.dto.response.TeamBySide;
import com.example.playgroundmanage.game.service.GameService;
import com.example.playgroundmanage.game.service.SubTeamService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import com.example.playgroundmanage.location.service.PlaygroundService;
import com.example.playgroundmanage.type.MatchTeamSide;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.playgroundmanage.date.MyDateTimeLocal.initMyDateTime;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;


    private final SubTeamService subTeamService;

    private final PlaygroundService playgroundService;



    @GetMapping("/game/{gameId}/{matchTeamSide}")
    public TeamBySide getSubTeams(@PathVariable Long gameId, @PathVariable String matchTeamSide) {
        List<SubTeamDto> subTeams =  gameService.getSubTeamsByTeamSide(gameId, MatchTeamSide.valueOf(matchTeamSide));
        SubTeamDto soloTeam = gameService.getSoloTeamByTeamSide(gameId, MatchTeamSide.valueOf(matchTeamSide));

        return TeamBySide.builder()
                .soloTeam(soloTeam)
                .subTeams(subTeams)
                .matchTeamSide(matchTeamSide)
                .build();
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

    @DeleteMapping("/game/{gameId}/{subTeamId}/out")
    public ResponseEntity<String> userOutOfGame(@PathVariable Long gameId, @AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Long subTeamId) {
        gameService.userOutOfGame(gameId, myUserDetails.getUser());
        subTeamService.deleteSubTeamIfParticipantZero(subTeamId);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/user/game/{year}/{month}")
    public List<UsersGameDto.UsersGameResponseDto> getMonthGameInLatestOrder(@PathVariable Integer month, @PathVariable Integer year, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        UsersGameDto.UsersGameRequestDto usersGameRequestDto = UsersGameDto.UsersGameRequestDto.builder()
                .myDateTime(initMyDateTime(LocalDateTime.of(year, month, 0, 0, 0)))
                .user(myUserDetails.getUser())
                .build();

        return gameService.getMonthGameAsc(usersGameRequestDto);
    }

}
