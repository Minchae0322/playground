package com.example.playgroundmanage.util;

import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.game.vo.User;

public class TeamValidation {

    public static void validateJoinTeam(Team team, User user) {
        boolean isMemberAlready = team.getMembers().stream()
                .anyMatch(teaming -> teaming.getUser().equals(user));

        if (isMemberAlready) {
            throw new RuntimeException("이미 팀에 존재합니다.");
        }
    }
}
