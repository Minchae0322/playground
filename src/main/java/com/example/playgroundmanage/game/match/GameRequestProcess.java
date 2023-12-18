package com.example.playgroundmanage.game.match;

import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.game.repository.MatchParticipantRepository;
import com.example.playgroundmanage.game.repository.SubTeamRepository;
import com.example.playgroundmanage.type.MatchTeamSide;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GameRequestProcess {

    private final MatchParticipantRepository matchParticipantRepository;

    private final SubTeamRepository subTeamRepository;

    public boolean isTeamAlreadyInMatchTeamQueue(CompetingTeam competingTeam, Team team) {
        return competingTeam.isContainTeam(team);
    }




    public boolean isUserParticipatingInGame(Game game, User user) {
        List<MatchParticipant> matchParticipantList = matchParticipantRepository.findAllByGame(game);
        return matchParticipantList.stream()
                .filter(p -> p.getUser().equals(user))
                .toList().size() != 0;
    }



}
