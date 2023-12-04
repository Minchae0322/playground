package com.example.playgroundmanage.match;

import com.example.playgroundmanage.repository.MatchParticipantRepository;
import com.example.playgroundmanage.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GameRequestProcess {

    private final MatchParticipantRepository matchParticipantRepository;

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

    public boolean isUserParticipatingInGame(Game game, User user) {
        List<MatchParticipant> matchParticipantList = matchParticipantRepository.findAllByGame(game);
        return matchParticipantList.stream()
                .filter(p -> p.getUser().equals(user))
                .toList().size() != 0;
    }



}
