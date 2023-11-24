package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.MatchParticipant;
import com.example.playgroundmanage.vo.MatchParticipantTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchParticipantTeamRepository extends JpaRepository<MatchParticipantTeam, Long> {
}
