package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.response.GameThumbnail;
import com.example.playgroundmanage.game.dto.GameResponseDto;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.login.dto.UsersGameDto;
import com.example.playgroundmanage.util.DateFormat;
import com.example.playgroundmanage.util.Util;
import org.springframework.stereotype.Component;

import static com.example.playgroundmanage.util.LocationFormatter.getLocation;

@Component
public class GameDtoConverter {

    public UsersGameDto.UsersGameResponseDto toUsersGameResponseDto(Game game) {
        return UsersGameDto.UsersGameResponseDto.builder()
                .gameId(game.getId())
                .playgroundId(game.getPlayground().getId())
                .location(getLocation(game))
                .gameType(game.getGameType())
                .localDateStartTime(game.getGameStartDateTime())
                .gameStart(DateFormat.dateFormatYYYYMMDDHHMM(game.getGameStartDateTime()))
                .hostName(game.getHost().getNickname())
                .gameName(game.getGameName())
                .runningTime(game.getRunningTime())
                .build();
    }

    public GameResponseDto toGameResponse(Game game) {
        return GameResponseDto.builder()
                .gameStartDateTime(Util.localDateToYearMonthDateTimeString(game.getGameStartDateTime()))
                .hostName(game.getHost().getNickname())
                .runningTime(game.getRunningTime())
                .gameName(game.getGameName())
                .gameType(game.getGameType().getValue())
                .playgroundId(game.getPlayground().getId())
                .playgroundName(game.getPlayground().getName())
                .campusName(game.getPlayground().getCampus().getCampusName())
                .sportsEvent(game.getSportsEvent().getValue_cn())
                .hostProfileImg(game.getHost().getUserProfileImg().getFileUrl())
                .gameId(game.getId())
                .build();
    }

}
