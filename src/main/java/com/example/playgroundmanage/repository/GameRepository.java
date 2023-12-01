package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.Game;
import com.example.playgroundmanage.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> , GameRepositoryCustom{

    List<Game> findAllByHostAndMatchStartAfter(User host, LocalDateTime matchStart);
}
