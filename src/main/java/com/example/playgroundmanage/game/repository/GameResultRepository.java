package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.game.vo.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultRepository extends JpaRepository<GameResult, Long> {

}
