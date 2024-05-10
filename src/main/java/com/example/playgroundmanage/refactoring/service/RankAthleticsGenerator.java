package com.example.playgroundmanage.refactoring.service;

import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.service.PlaygroundService;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.refactoring.Athletics;
import com.example.playgroundmanage.refactoring.AthleticsSide;
import com.example.playgroundmanage.refactoring.GameGenerationRequest;
import com.example.playgroundmanage.refactoring.RankAthletics;
import com.example.playgroundmanage.refactoring.repo.AthleticsRepository;
import com.example.playgroundmanage.refactoring.repo.AthleticsSideRepository;
import com.example.playgroundmanage.refactoring.repo.RankAthleticsRepository;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.util.GameValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RankAthleticsGenerator implements AthleticsGenerator{

    private final AthleticsRepository athleticsRepository;

    private final GameValidation gameValidation;

    private final PlaygroundRepository playgroundRepository;

    private final AthleticsSideRepository athleticsSideRepository;

    private final UserRepository userRepository;

    @Transactional
    public void generate(final Long hostId, final GameGenerationRequest gameGenerationRequest) {
        Playground playground = playgroundRepository.findById(gameGenerationRequest.playgroundId())
                .orElseThrow(PlaygroundNotExistException::new);

        gameValidation.validateOverlappingGames(playground.getGames(), gameGenerationRequest.toGameTimeDto());

        User host = userRepository.findById(hostId)
                .orElseThrow(UserNotExistException::new);

        final RankAthletics athletics = RankAthletics.of(
                host,
                gameGenerationRequest.gameName(),
                gameGenerationRequest.sportsEvent(),
                gameGenerationRequest.startDateTime().getLocalDateTime(),
                gameGenerationRequest.runningTime(),
                playground
        );

        athleticsRepository.save(athletics);
        generateCompetingTeams(athletics);
    }

    private void generateCompetingTeams(RankAthletics athletics) {
        Arrays.stream(GameTeamSide.values())
                .forEach(gameTeamSide -> {
                    AthleticsSide athleticsSide = AthleticsSide.builder()
                            .gameTeamSide(gameTeamSide)
                            .athletics(athletics)
                            .build();
                    athleticsSideRepository.save(athleticsSide);
                    athletics.addAthleticsSide(athleticsSide);
                });
    }

}
