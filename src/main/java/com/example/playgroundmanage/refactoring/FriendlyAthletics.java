package com.example.playgroundmanage.refactoring;


import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameType;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class FriendlyAthletics extends Athletics {


    @Builder
    public FriendlyAthletics(Long id, String gameName, SportsEvent sportsEvent, GameType gameType, LocalDateTime gameStartDateTime, Integer runningTime, Playground playground, User host, List<GameParticipant> gameParticipants) {
        super(id, gameName, sportsEvent, gameType, gameStartDateTime, runningTime, playground, host, gameParticipants);
    }
}
