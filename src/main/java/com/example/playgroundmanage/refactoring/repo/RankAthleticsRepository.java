package com.example.playgroundmanage.refactoring.repo;

import com.example.playgroundmanage.refactoring.RankAthletics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankAthleticsRepository extends JpaRepository<RankAthletics, Long> {
}
