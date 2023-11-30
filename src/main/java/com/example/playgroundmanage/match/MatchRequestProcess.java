package com.example.playgroundmanage.match;

import com.example.playgroundmanage.vo.*;
import org.springframework.stereotype.Component;

@Component
public class MatchRequestProcess {

    public boolean isTeamAlreadyInMatchTeamQueue(CompetingTeam competingTeam, Team team) {
        return competingTeam.isContainTeam(team);
    }

    public MatchParticipant joinSubTeam(SubTeam subTeam, User user) {
        if(containUserInSubTeam(subTeam, user)) {
            throw new IllegalArgumentException("팀에 이미 참여했습니다.");
        }
        return MatchParticipant.builder()
                .user(user)
                .subTeam(subTeam)
                .build();
    }

    private boolean containUserInSubTeam(SubTeam subTeam, User user) {
        return subTeam.getMatchParticipants().stream()
                .filter(p -> p.getUser().equals(user))
                .toList().size() != 0;
    }



}
