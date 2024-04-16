package com.example.playgroundmanage.game.service.impl;

import com.example.playgroundmanage.game.dto.GameDto;
import com.example.playgroundmanage.game.dto.GameTeamRequestDto;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.game.service.GameGenerator;
import com.example.playgroundmanage.game.dto.GameTeamResponseDto;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.login.dto.UserResponseDto;
import com.example.playgroundmanage.login.service.UserService;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.util.GameValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendlyGameGenerator implements GameGenerator {

    private final GameRepository gameRepository;

    private final PlaygroundRepository playgroundRepository;

    private final GameValidation gameValidation;



    @Override
    public String getType() {
        return "Friendly";
    }

    @Override
    public Long generate(GameDto gameDto) {
        Playground playground = playgroundRepository.findById(gameDto.getPlaygroundId())
                .orElseThrow(PlaygroundNotExistException::new);

        gameValidation.validateOverlappingGames(playground.getGames(), gameDto.toGameDateDto());

        Game game = Game.builder()
                .gameName(gameDto.getGameName())
                .host(gameDto.getHost())
                .playground(playground)
                .gameType(GameType.FRIENDLY)
                .gameStartDateTime(gameDto.getStartDateTime().getLocalDateTime())
                .sportsEvent(gameDto.getSportsEvent())
                .runningTime(gameDto.getRunningTime())
                .build();

        return gameRepository.save(game).getId();
    }

    @Override
    public GameTeamResponseDto getGameTeamInfos(GameTeamRequestDto gameTeamRequestDto) {
        Game game = gameRepository.findById(gameTeamRequestDto.getGameId())
                .orElseThrow(GameNotExistException::new);

        List<UserResponseDto> userInfoDtos = game.getGameParticipants().stream()
                .map(GameParticipant::toUserResponseDto)
                .toList();

        return GameTeamResponseDto.builder()
                .participants(userInfoDtos)
                .build();
    }

}
