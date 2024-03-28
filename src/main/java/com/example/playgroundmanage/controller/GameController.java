package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.date.MyDateTime;
import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.dto.GameTimeDto;
import com.example.playgroundmanage.login.dto.UsersGameDto;
import com.example.playgroundmanage.dto.reqeust.GameRegistration;
import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.game.GameGenerator;
import com.example.playgroundmanage.game.GameGeneratorFactory;
import com.example.playgroundmanage.game.GameResultManagerFactory;
import com.example.playgroundmanage.game.GameResultManger;
import com.example.playgroundmanage.game.dto.GameResultDto;
import com.example.playgroundmanage.game.dto.GameTeamResponseDto;
import com.example.playgroundmanage.game.service.GameService;
import com.example.playgroundmanage.game.service.SubTeamService;
import com.example.playgroundmanage.login.service.UserService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import com.example.playgroundmanage.location.service.PlaygroundService;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.playgroundmanage.date.MyDateTimeLocal.initMyDateTime;

@RestController
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    private final UserService userService;
    private final SubTeamService subTeamService;

    private final PlaygroundService playgroundService;
    private final GameGeneratorFactory gameGeneratorFactory;

    private final GameResultManagerFactory gameResultManagerFactory;



    /*@GetMapping("/game/{gameId}/{gameTeamSide}")
    public TeamBySide getSubTeams(@PathVariable Long gameId, @PathVariable String gameTeamSide) {
        List<SubTeamDto> subTeams = gameService.getSubTeamsByTeamSide(gameId, GameTeamSide.valueOf(gameTeamSide));
        SubTeamDto soloTeam = gameService.getSoloTeamByTeamSide(gameId, GameTeamSide.valueOf(gameTeamSide));

        return TeamBySide.builder()
                .soloTeam(soloTeam)
                .subTeams(subTeams)
                .matchTeamSide(gameTeamSide)
                .build();
    }*/

    @GetMapping("/game/{gameId}/{gameType}/{gameTeamSide}")
    public GameTeamResponseDto getGameTeam(@PathVariable Long gameId, @PathVariable String gameType, @PathVariable String gameTeamSide) {
        GameGenerator gameGenerator = gameGeneratorFactory.find(gameType);

        return gameGenerator.getGameTeamInfos(gameId, GameTeamSide.valueOf(gameTeamSide));
    }

    @GetMapping("/game/{gameId}/info")
    public GameThumbnail getGameInfo(@PathVariable Long gameId) {
        GameDto gameDto = gameService.getGameInfo(gameId);

        return gameDto.toGameThumbnail();
    }

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
                .startDateTime(gameDto.getStartDateTime())
                .build();

        playgroundService.isValidGameStartTime(gameDto.getPlaygroundId(), gameTimeDto);
        GameGenerator gameGenerator = gameGeneratorFactory.find(gameRegistration.getGameType());

        return ResponseEntity.ok(gameGenerator.generate(gameDto));
    }

    @DeleteMapping("/game/{gameId}/{subTeamId}/out")
    @Transactional
    public ResponseEntity<String> userOutOfGame(@PathVariable Long gameId, @AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Long subTeamId) {
        gameService.userOutOfGame(gameId, myUserDetails.getUser());
        subTeamService.deleteSubTeamIfParticipantZero(subTeamId);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/user/game/{year}/{month}")
    public List<UsersGameDto.UsersGameResponseDto> getMonthGamesInLatestOrder(@PathVariable Integer month, @PathVariable Integer year, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        UsersGameDto.UsersGameRequestDto usersGameRequestDto = UsersGameDto.UsersGameRequestDto.builder()
                .myDateTime(initMyDateTime(LocalDateTime.of(year, month, 1, 0, 0)))
                .user(myUserDetails.getUser())
                .build();

        return gameService.getMonthGameAsc(usersGameRequestDto);
    }

    @GetMapping("/user/game/host/{year}/{month}")
    public List<UsersGameDto.UsersGameResponseDto> getGamesUserHostByDate(@AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Integer year, @PathVariable Integer month) {
        return gameService.getGamesUserHostByDate(myUserDetails.getUser(), LocalDateTime.of(year, month, 1, 0, 0));
    }

    @GetMapping("/user/game/host")
    public List<UsersGameDto.UsersGameResponseDto> getGamesUserHost(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        return gameService.getGamesUserHost(myUserDetails.getUser());
    }

    @PreAuthorize("hasPermission(#gameId,'delete_game','DELETE')")
    @DeleteMapping("/user/game/{gameId}/delete")
    //todo 유저가 호스트 하는 게임이랑 유저가 참여하는 게임 프론트 엔드 완성
    public void deleteGame(@AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Long gameId) {
        gameService.deleteGame(gameId);
    }


    @PostMapping("/game/result/{gameType}")
    public void summitGameResult(@RequestBody GameResultDto.GameResultRequestDto gameResultRequestDto, @PathVariable String gameType) {
        GameResultManger gameResultManger = gameResultManagerFactory.find(gameType);

        gameResultManger.submitGameResult(gameResultRequestDto);
    }
}
