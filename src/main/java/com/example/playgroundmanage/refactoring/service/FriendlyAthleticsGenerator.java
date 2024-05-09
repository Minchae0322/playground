package com.example.playgroundmanage.refactoring.service;

import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.service.HostService;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.refactoring.Athletics;
import com.example.playgroundmanage.refactoring.FriendlyAthletics;
import com.example.playgroundmanage.refactoring.GameGenerationRequest;
import com.example.playgroundmanage.refactoring.RankAthletics;
import com.example.playgroundmanage.refactoring.repo.AthleticsRepository;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.util.GameValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FriendlyAthleticsGenerator implements AthleticsGenerator {

    private final PlaygroundRepository playgroundRepository;

    private final GameValidation gameValidation;

    private final AthleticsRepository athleticsRepository;

    private final HostService hostService;

    @Override
    public void generate(GameGenerationRequest gameGenerationRequest) {
        Playground playground = playgroundRepository.findById(gameGenerationRequest.playgroundId())
                .orElseThrow(PlaygroundNotExistException::new);

        gameValidation.validateOverlappingGames(playground.getGames(), gameGenerationRequest.toGameTimeDto());

        User host = hostService.getGameHost(gameGenerationRequest.hostId());

        FriendlyAthletics athletics = (FriendlyAthletics) toEntity(gameGenerationRequest, host, playground);
        athleticsRepository.save(athletics);
    }

    private Athletics toEntity(GameGenerationRequest gameGenerationRequest, User host, Playground playground) {
        return RankAthletics.builder()
                .gameName(gameGenerationRequest.gameName())
                .host(host)
                .playground(playground)
                .gameType(GameType.COMPETITION)
                .gameStartDateTime(gameGenerationRequest.startDateTime().getLocalDateTime())
                .sportsEvent(gameGenerationRequest.sportsEvent())
                .runningTime(gameGenerationRequest.runningTime())
                .build();
    }

}
