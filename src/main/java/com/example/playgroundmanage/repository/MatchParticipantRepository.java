package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.Game;
import com.example.playgroundmanage.vo.MatchParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {

    List<MatchParticipant> findAllByGame(Game game);
}
