package com.example.playgroundmanage.team.repository;

import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.type.SportsEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findById(Long teamId);

    boolean existsByTeamName(String teamName);

    List<Team> findAllBySportsEvent(SportsEvent sportsEvent);
}
