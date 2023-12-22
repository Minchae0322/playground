package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.game.repository.TeamingRepository;
import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.game.vo.Teaming;
import com.example.playgroundmanage.game.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamingService {
    private final TeamingRepository teamingRepository;

    public Teaming joinTeam(Team team, User user) {
        Teaming teaming = Teaming.builder()
                .user(user)
                .team(team)
                .role("Leader")
                .build();
        return teamingRepository.save(teaming);
    }

    public List<Teaming> getTeamUserRelations(Team team) {
        return teamingRepository.findAllByTeam(team);
    }

    public List<Teaming> getTeamUserRelations(User user) {
        return teamingRepository.findAllByUser(user);
    }

}
