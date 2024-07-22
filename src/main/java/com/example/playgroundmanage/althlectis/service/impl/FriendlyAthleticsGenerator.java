package com.example.playgroundmanage.althlectis.service.impl;

import com.example.playgroundmanage.althlectis.dto.GameTimeDto;
import com.example.playgroundmanage.althlectis.service.AthleticsGenerator;
import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.service.TimeValidation;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.althlectis.vo.impl.FriendlyAthletics;
import com.example.playgroundmanage.althlectis.dto.request.GameGenerationRequest;
import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.util.GameValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendlyAthleticsGenerator implements AthleticsGenerator {

    private final PlaygroundRepository playgroundRepository;

    private final TimeValidation timeValidation;

    private final AthleticsRepository athleticsRepository;

    private final UserRepository userRepository;


    @Override
    public String getType() {
        return "Friendly";
    }

    @Override
    @Transactional
    public Long generate(final Long hostId, final GameGenerationRequest gameGenerationRequest) {
        final Playground playground = playgroundRepository.findById(gameGenerationRequest.playgroundId())
                .orElseThrow(PlaygroundNotExistException::new);

        List<GameTimeDto> playgroundTimeTable = playgroundRepository.getPlaygroundTimeTable(gameGenerationRequest.playgroundId());

        timeValidation.validateOverlappingGames(playgroundTimeTable, gameGenerationRequest.toGameTimeDto());

        final User host = userRepository.findById(hostId)
                .orElseThrow(UserNotExistException::new);

        final FriendlyAthletics athletics = FriendlyAthletics.of(
                host,
                gameGenerationRequest.gameName(),
                SportsEvent.fromString(gameGenerationRequest.sportsEvent()),
                gameGenerationRequest.toGameTimeDto().getStartDateTime(),
                gameGenerationRequest.runningTime(),
                playground,
                GameType.FRIENDLY
        );

        return athleticsRepository.save(athletics).getId();
    }


}
