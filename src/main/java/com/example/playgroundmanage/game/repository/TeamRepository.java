package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.game.vo.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long>, TeamRepositoryCustom {
    Optional<Team> findById(Long teamId);
}
