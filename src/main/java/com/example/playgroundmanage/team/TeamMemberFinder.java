package com.example.playgroundmanage.team;

import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.team.vo.Team;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamMemberFinder {

    public boolean isTeamMember(Team team, User user) {
        return team.getMembers().stream()
                .anyMatch(teamMember -> teamMember.getUser().equals(user));
    }
}
