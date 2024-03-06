package com.example.playgroundmanage.team;

import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.team.vo.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamMemberFinder {

    public boolean isTeamMember(Team team, User user) {
        return team.getMembers().stream()
                .anyMatch(teamMember -> teamMember.getUser().equals(user));
    }

    public boolean isTeamLeader(Team team, User user) {
        return team.getLeader().equals(user);
    }
}
