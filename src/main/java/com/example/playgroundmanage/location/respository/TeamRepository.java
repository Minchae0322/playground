package com.example.playgroundmanage.location.respository;

import com.example.playgroundmanage.team.vo.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findById(Long teamId);

    boolean existsByTeamName(String teamName);
}
