package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
