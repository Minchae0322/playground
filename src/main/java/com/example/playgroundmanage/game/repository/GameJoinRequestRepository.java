package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameJoinRequest;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.game.vo.impl.TeamGameJoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GameJoinRequestRepository extends JpaRepository<GameJoinRequest, Long> {

    Optional<GameJoinRequest> findByGameAndUser(Game game, User user);



    List<GameJoinRequest> findAllByUserAndExpiredTimeAfter(User user, LocalDateTime localDateTime);
}
