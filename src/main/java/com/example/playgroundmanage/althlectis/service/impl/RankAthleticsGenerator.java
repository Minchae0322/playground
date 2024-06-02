package com.example.playgroundmanage.althlectis.service.impl;

import com.example.playgroundmanage.althlectis.service.AthleticsGenerator;
import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.althlectis.vo.AthleticsSide;
import com.example.playgroundmanage.althlectis.dto.request.GameGenerationRequest;
import com.example.playgroundmanage.althlectis.vo.impl.RankAthletics;
import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.althlectis.repo.AthleticsSideRepository;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.util.GameValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class RankAthleticsGenerator implements AthleticsGenerator {

    private final AthleticsRepository athleticsRepository;

    private final GameValidation gameValidation;

    private final PlaygroundRepository playgroundRepository;

    private final AthleticsSideRepository athleticsSideRepository;

    private final UserRepository userRepository;

    @Override
    public String getType() {
        return "Competition";
    }

    @Transactional
    public Long generate(final Long hostId, final GameGenerationRequest gameGenerationRequest) {
        Playground playground = playgroundRepository.findById(gameGenerationRequest.playgroundId())
                .orElseThrow(PlaygroundNotExistException::new);

        gameValidation.validateOverlappingGames(playground.getGames(), gameGenerationRequest.toGameTimeDto());

        User host = userRepository.findById(hostId)
                .orElseThrow(UserNotExistException::new);

        final RankAthletics athletics = RankAthletics.of(
                host,
                gameGenerationRequest.gameName(),
                SportsEvent.fromString(gameGenerationRequest.sportsEvent()),
                gameGenerationRequest.gameStartDateTime().toLocalDateTime(),
                gameGenerationRequest.runningTime(),
                playground,
                GameType.COMPETITION
        );

        RankAthletics rankAthletics = athleticsRepository.save(athletics);
        generateAthleticsSide(athletics);

        return rankAthletics.getId();
    }
    private void generateAthleticsSide(RankAthletics athletics) {
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
