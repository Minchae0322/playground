package com.example.playgroundmanage.service;

import com.example.playgroundmanage.repository.TeamingRepository;
import com.example.playgroundmanage.vo.Team;
import com.example.playgroundmanage.vo.Teaming;
import com.example.playgroundmanage.vo.User;
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
                .build();
        return teamingRepository.save(teaming);
    }

    public List<Teaming> getTeamUserRelations(Team team) {
        return teamingRepository.findAllByTeam(team);
    }

}
