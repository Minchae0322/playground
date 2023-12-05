package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.MatchParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {

    List<MatchParticipant> findAllByGame(Game game);
}
