package com.example.playgroundmanage.game;

import com.example.playgroundmanage.dto.GameDto;
import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.game.repository.CompetingTeamRepository;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.SubTeamRepository;
import com.example.playgroundmanage.game.service.SubTeamService;
import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.util.GameValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompetitionGameGenerator implements GameGenerator {

    private final GameRepository gameRepository;

    private final PlaygroundRepository playgroundRepository;

    private final GameValidation gameValidation;

    private final SubTeamService subTeamService;

    private final CompetingTeamRepository competingTeamRepository;



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

    private Game generateGame(GameDto gameDto) {
        Playground playground = playgroundRepository.findById(gameDto.getPlaygroundId())
                .orElseThrow(PlaygroundNotExistException::new);

        gameValidation.validateOverlappingGames(playground.getGames(), gameDto.toGameDateDto());

        Game game = Game.builder()
                .gameName(gameDto.getGameName())
                .host(gameDto.getHost())
                .playground(playground)
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
