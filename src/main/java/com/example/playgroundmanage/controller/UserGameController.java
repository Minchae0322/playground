package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.game.service.SubTeamService;
import com.example.playgroundmanage.login.dto.UsersGameDto;
import com.example.playgroundmanage.login.service.UserHostGameService;
import com.example.playgroundmanage.login.service.UserJoinGameService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.playgroundmanage.date.MyDateTimeLocal.initMyDateTime;

@RestController
@RequiredArgsConstructor
public class UserGameController {

    private final UserHostGameService userHostGameService;

    private final UserJoinGameService userJoinGameService;

    private final SubTeamService subTeamService;

    @GetMapping("/user/game/{year}/{month}")
    public List<UsersGameDto.UsersGameResponseDto> getMonthGamesInLatestOrder(@PathVariable Integer month, @PathVariable Integer year, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        UsersGameDto.UsersGameRequestDto usersGameRequestDto = UsersGameDto.UsersGameRequestDto.builder()
                .myDateTime(initMyDateTime(LocalDateTime.of(year, month, 1, 0, 0)))
                .user(myUserDetails.getUser())
                .build();

        return userJoinGameService.getUserJoinGames(usersGameRequestDto);
    }


    @GetMapping("/user/game/host/{year}/{month}")
    public List<UsersGameDto.UsersGameResponseDto> getGamesUserHostByDate(@AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Integer year, @PathVariable Integer month) {
        return userHostGameService.getUserHostGamesInMonth(myUserDetails.getUser(), LocalDateTime.of(year, month, 1, 0, 0));
    }

    @GetMapping("/user/game/host")
    public List<UsersGameDto.UsersGameResponseDto> getGamesUserHost(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        return userHostGameService.getUserHostGames(myUserDetails.getUser());
    }

    @DeleteMapping("/game/{gameId}/{subTeamId}/out")
    @Transactional
    public ResponseEntity<String> userOutOfGame(@PathVariable Long gameId, @AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Long subTeamId) {
        userJoinGameService.userOutOfGame(gameId, myUserDetails.getUser());
        subTeamService.deleteSubTeamIfParticipantZero(subTeamId);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/game/{gameId}/friendly/out")
    @Transactional
    public ResponseEntity<String> userOutOfFriendlyGame(@PathVariable Long gameId, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        userJoinGameService.userOutOfGame(gameId, myUserDetails.getUser());
        return ResponseEntity.ok("success");
    }
}
