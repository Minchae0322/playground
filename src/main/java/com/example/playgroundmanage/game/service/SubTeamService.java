package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.exception.MatchNotExistException;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.repository.SubTeamRepository;
import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.SubTeam;
import jakarta.persistence.Version;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubTeamService {

    private final SubTeamRepository subTeamRepository;

    private final GameRepository gameRepository;

    @Transactional
    @Version
    public void generateSoloSubTeamInCompetingTeam(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(MatchNotExistException::new);

        game.getCompetingTeams().forEach(this::generateSoloSubTeam);
    }

    private void generateSoloSubTeam(CompetingTeam competingTeam) {
        subTeamRepository.save(SubTeam.builder()
                .isSoloTeam(true)
                .isAccept(true)
                .competingTeam(competingTeam)
                .build());
    }

    @Transactional
    public void deleteSubTeamIfParticipantZero(Long subTeamId) {
        SubTeam subTeam = subTeamRepository.findById(subTeamId)
                .orElseThrow();
        if (subTeam.getGameParticipants().size() == 0) {
            subTeam.getCompetingTeam().getSubTeams().remove(subTeam);
            subTeamRepository.delete(subTeam);
        }
    }


}
