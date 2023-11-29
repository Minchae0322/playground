package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.CompetingTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchTeamRepository extends JpaRepository<CompetingTeam, Long> {
}
