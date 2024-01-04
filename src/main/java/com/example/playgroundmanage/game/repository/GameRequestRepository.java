package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameRequest;
import com.example.playgroundmanage.game.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GameRequestRepository extends JpaRepository<GameRequest, Long> {

    Optional<GameRequest> findByGameAndUser(Game game, User user);



    List<GameRequest> findAllByHostAndExpiredTimeAfter(User user, LocalDateTime localDateTime);
}
