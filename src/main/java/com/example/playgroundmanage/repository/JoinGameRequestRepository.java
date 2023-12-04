package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.Game;
import com.example.playgroundmanage.vo.JoinGameRequest;
import com.example.playgroundmanage.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JoinGameRequestRepository extends JpaRepository<JoinGameRequest, Long> {

    Optional<JoinGameRequest> findByGameAndUser(Game game, User user);

    List<JoinGameRequest> findAllByUserAndExpiredTimeAfter(User user, LocalDateTime localDateTime);
}
