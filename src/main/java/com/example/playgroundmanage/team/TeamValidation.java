package com.example.playgroundmanage.team;

import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.login.vo.User;

public class TeamValidation {

    public static void validateJoinTeam(Team team, User user) {
        boolean isMemberAlready = team.getMembers().stream()
                .anyMatch(teaming -> teaming.getUser().equals(user));

        if (isMemberAlready) {
            throw new RuntimeException("이미 팀에 존재합니다.");
        }
    }

    public static void validateUserInTeam(Team team, User user) {
        boolean isTeamMember = team.getMembers().stream()
                .anyMatch(teaming -> teaming.getUser().equals(user));
        if (!isTeamMember) {
            throw new RuntimeException("팀 멤버가 아닙니다.");
        }
    }
}
