package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
