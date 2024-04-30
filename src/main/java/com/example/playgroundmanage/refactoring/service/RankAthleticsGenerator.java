package com.example.playgroundmanage.refactoring.service;

import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.location.service.PlaygroundService;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.refactoring.Athletics;
import com.example.playgroundmanage.refactoring.GameGenerationRequest;
import com.example.playgroundmanage.refactoring.repo.AthleticsRepository;
import com.example.playgroundmanage.refactoring.repo.RankAthleticsRepository;
import com.example.playgroundmanage.util.GameValidation;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RankAthleticsGenerator {

    private final AthleticsRepository athleticsRepository;

    private final GameValidation gameValidation;

    private final PlaygroundRepository playgroundRepository;


    @Transactional
    public void generate(GameGenerationRequest gameGenerationRequest) {
        Playground playground = playgroundRepository.findById(gameGenerationRequest.playgroundId())
                .orElseThrow(PlaygroundNotExistException::new);

        gameValidation.validateOverlappingGames(playground.getGames(), gameGenerationRequest.toGameTimeDto());

        Athletics athletics = gameGenerationRequest.toEntity();
        athleticsRepository.save(athletics);
        //generateCompetingTeams(athletics);


    }

/*    private void generateCompetingTeams(Athletics athletics) {
        CompetingTeam competingTeam = CompetingTeam.builder()
                .game(athletics)
                .gameTeamSide(gameTeamSide)
                .build();
        competingTeamRepository.save(competingTeam);
        athletics.addCompetingTeam(competingTeam);
    }*/





}
