package com.example.playgroundmanage.game;

import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.dto.SubTeamDto;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.game.dto.GameTeamResponseDto;
import com.example.playgroundmanage.game.repository.CompetingTeamRepository;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.SubTeamRepository;
import com.example.playgroundmanage.game.service.SubTeamService;
import com.example.playgroundmanage.game.service.UserService;
import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.util.GameValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class CompetitionGameGenerator implements GameGenerator {

    private final GameRepository gameRepository;

    private final PlaygroundRepository playgroundRepository;

    private final GameValidation gameValidation;

    private final SubTeamService subTeamService;

    private final CompetingTeamRepository competingTeamRepository;

    private final FileHandler fileHandler;

    private final UserService userService;


    @Override
    public String getType() {
        return "Competition";
    }

    @Override
    @Transactional
    public Long generate(GameDto gameDto) {
        Game newGame = generateGame(gameDto);
        generateCompetingTeam(newGame, GameTeamSide.HOME);
        generateCompetingTeam(newGame, GameTeamSide.AWAY);

        subTeamService.generateSoloSubTeamInCompetingTeam(newGame.getId());
        return newGame.getId();
    }

    @Override
    public GameTeamResponseDto getGameTeamInfos(Long gameId, GameTeamSide gameTeamSide) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        CompetingTeam competingTeam = game.getCompetingTeamBySide(gameTeamSide)
                .orElseThrow();

        List<SubTeamDto> subTeams = competingTeam.getSubTeamsNotSoloTeam().stream()
                .map(this::toSubTeamDto)
                .toList();

        SubTeamDto soloTeam = toSubTeamDtoSolo(competingTeam.getSoloTeam());

        return GameTeamResponseDto.builder()
                .subTeams(subTeams)
                .soloTeam(soloTeam)
                .build();
    }

    private SubTeamDto toSubTeamDto(SubTeam subTeam) {
        return SubTeamDto.builder()
                .subTeamId(subTeam.getId())
                .teamId(subTeam.getTeam().getId())
                .teamName(subTeam.getTeam().getTeamName())
                .teamProfileImg(fileHandler.extractFile(subTeam.getTeam().getTeamPic()))
                .teamDescription(subTeam.getTeam().getDescription())
                .users(subTeam.getGameParticipants().stream()
                        .map(gameParticipant -> userService.getUserInfoInTeam(subTeam.getTeam(), gameParticipant.getUser()))
                        .toList())
                .build();
    }

    private SubTeamDto toSubTeamDtoSolo(SubTeam subTeam) {
        return SubTeamDto.builder()
                .subTeamId(subTeam.getId())
                .users(subTeam.getGameParticipants().stream()
                        .map(gameParticipant -> userService.getUserInfo(gameParticipant.getUser()))
                        .toList())
                .build();
    }

    private Game generateGame(GameDto gameDto) {
        Playground playground = playgroundRepository.findById(gameDto.getPlaygroundId())
                .orElseThrow(PlaygroundNotExistException::new);

        gameValidation.validateOverlappingGames(playground.getGames(), gameDto.toGameDateDto());

        Game game = Game.builder()
                .gameName(gameDto.getGameName())
                .host(gameDto.getHost())
                .playground(playground)
                .gameType(GameType.COMPETITION)
                .gameStartDateTime(gameDto.getStartDateTime().getLocalDateTime())
                .sportsEvent(gameDto.getSportsEvent())
                .runningTime(gameDto.getRunningTime())
                .build();
        return gameRepository.save(game);
    }

    @Transactional
    private void generateCompetingTeam(Game game, GameTeamSide gameTeamSide) {
        CompetingTeam competingTeam = CompetingTeam.builder()
                .game(game)
                .gameTeamSide(gameTeamSide)
                .build();
        competingTeamRepository.save(competingTeam);
        game.addCompetingTeam(competingTeam);
    }


}
