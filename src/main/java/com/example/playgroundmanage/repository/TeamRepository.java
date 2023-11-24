package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
