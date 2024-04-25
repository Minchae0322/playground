package com.example.playgroundmanage.controller;

import com.example.playgroundmanage.login.dto.UsersGameDto;
import com.example.playgroundmanage.login.service.UserHostGameService;
import com.example.playgroundmanage.login.service.UserJoinGameService;
import com.example.playgroundmanage.login.vo.MyUserDetails;
import lombok.RequiredArgsConstructor;
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


    @GetMapping("/user/game/{year}/{month}")
    public List<UsersGameDto.UsersGameResponseDto> getMonthGamesInLatestOrder(@PathVariable Integer month, @PathVariable Integer year, @AuthenticationPrincipal MyUserDetails myUserDetails) {
        UsersGameDto.UsersGameRequestDto usersGameRequestDto = UsersGameDto.UsersGameRequestDto.builder()
                .myDateTime(initMyDateTime(LocalDateTime.of(year, month, 1, 0, 0)))
                .user(myUserDetails.getUser())
                .build();

        return userJoinGameService.getUserJoinGames(usersGameRequestDto);
    }



    @PreAuthorize("hasPermission(#gameId,'delete_game','DELETE')")
    @DeleteMapping("/user/game/{gameId}/delete")
    //todo 유저가 호스트 하는 게임이랑 유저가 참여하는 게임 프론트 엔드 완성
    public void deleteGame(@AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Long gameId) {
        userJoinGameService.deleteGame(gameId);
    }

    @GetMapping("/user/game/host/{year}/{month}")
    public List<UsersGameDto.UsersGameResponseDto> getGamesUserHostByDate(@AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Integer year, @PathVariable Integer month) {
        return userHostGameService.getUserHostGamesInMonth(myUserDetails.getUser(), LocalDateTime.of(year, month, 1, 0, 0));
    }

    @GetMapping("/user/game/host")
    public List<UsersGameDto.UsersGameResponseDto> getGamesUserHost(@AuthenticationPrincipal MyUserDetails myUserDetails) {
        return userHostGameService.getUserHostGames(myUserDetails.getUser());
    }
}
