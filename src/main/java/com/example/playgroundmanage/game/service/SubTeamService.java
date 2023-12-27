package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.exception.MatchNotExistException;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.SubTeamRepository;
import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.SubTeam;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubTeamService {

    private final SubTeamRepository subTeamRepository;

    private final GameRepository gameRepository;

    @Transactional
    public void generateSoloSubTeamBothCompetingTeam(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(MatchNotExistException::new);

        if (game.getHomeTeam().isContainSoloTeam() || game.getAwayTeam().isContainSoloTeam()) {
            throw new IllegalArgumentException("개인 팀이 이미 있습니다.");
        }

        subTeamRepository.save(generateSoloSubTeam(game.getHomeTeam()));
        subTeamRepository.save(generateSoloSubTeam(game.getAwayTeam()));
    }

    private SubTeam generateSoloSubTeam(CompetingTeam competingTeam) {
        return SubTeam.builder()
                .isSoloTeam(true)
                .isAccept(true)
                .competingTeam(competingTeam)
                .build();
    }


}
