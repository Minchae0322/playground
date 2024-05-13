package com.example.playgroundmanage.refactoring.service;

import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.refactoring.FriendlyAthletics;
import com.example.playgroundmanage.refactoring.GameGenerationRequest;
import com.example.playgroundmanage.refactoring.repo.AthleticsRepository;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.util.GameValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FriendlyAthleticsGenerator implements AthleticsGenerator {

    private final PlaygroundRepository playgroundRepository;

    private final GameValidation gameValidation;

    private final AthleticsRepository athleticsRepository;

    private final UserRepository userRepository;


    @Override
    public String getType() {
        return "Friendly";
    }

    @Override
    public Long generate(final Long hostId, final GameGenerationRequest gameGenerationRequest) {
        final Playground playground = playgroundRepository.findById(gameGenerationRequest.playgroundId())
                .orElseThrow(PlaygroundNotExistException::new);

        gameValidation.validateOverlappingGames(playground.getGames(), gameGenerationRequest.toGameTimeDto());

        final User host = userRepository.findById(hostId)
                .orElseThrow(UserNotExistException::new);

        final FriendlyAthletics athletics = FriendlyAthletics.of(
                host,
                gameGenerationRequest.gameName(),
                SportsEvent.fromString(gameGenerationRequest.sportsEvent()),
                gameGenerationRequest.gameStartDateTime(),
                gameGenerationRequest.runningTime(),
                playground,
                GameType.FRIENDLY
        );

        return athleticsRepository.save(athletics).getId();
    }


}
