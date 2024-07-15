package com.example.playgroundmanage.althlectis.service.impl;

import com.example.playgroundmanage.althlectis.service.AthleticsGenerator;

import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.service.TimeValidation;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.althlectis.dto.request.GameGenerationRequest;
import com.example.playgroundmanage.althlectis.vo.impl.RankAthletics;
import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;

@Component
@RequiredArgsConstructor
public class RankAthleticsGenerator implements AthleticsGenerator {

    private final AthleticsRepository athleticsRepository;

    private final TimeValidation timeValidation;

    private final PlaygroundRepository playgroundRepository;

    private final UserRepository userRepository;

    @Override
    public String getType() {
        return "Competition";
    }


    @Transactional
    @Override
    public Long generate(final Long hostId, final GameGenerationRequest gameGenerationRequest)  {

            Playground playground = playgroundRepository.findById(gameGenerationRequest.playgroundId())
                    .orElseThrow(PlaygroundNotExistException::new);

            timeValidation.validateOverlappingGames(playground.getAthletics(), gameGenerationRequest.toGameTimeDto());

            User host = userRepository.findById(hostId)
                    .orElseThrow(UserNotExistException::new);

            final RankAthletics athletics = RankAthletics.of(
                    host,
                    gameGenerationRequest.gameName(),
                    SportsEvent.fromString(gameGenerationRequest.sportsEvent()),
                    gameGenerationRequest.toGameTimeDto().getStartDateTime(),
                    gameGenerationRequest.runningTime(),
                    playground,
                    GameType.COMPETITION
            );

            RankAthletics rankAthletics = athleticsRepository.save(athletics);

            return rankAthletics.getId();

    }


}
