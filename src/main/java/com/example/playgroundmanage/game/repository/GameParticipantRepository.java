package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.login.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameParticipantRepository extends JpaRepository<GameParticipant, Long> {

    List<GameParticipant> findAllByGame(Game game);

    List<GameParticipant> findAllBySubTeam(SubTeam subTeam);

    List<GameParticipant> findAllByUser(User user);

    Optional<GameParticipant> findByGameAndUser(Game game, User user);

}
