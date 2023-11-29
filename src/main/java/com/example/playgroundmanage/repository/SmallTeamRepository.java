package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.SubTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmallTeamRepository extends JpaRepository<SubTeam, Long> {
}
