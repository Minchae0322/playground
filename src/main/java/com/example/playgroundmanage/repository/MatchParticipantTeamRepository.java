package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.MatchParticipant;
import com.example.playgroundmanage.vo.MatchParticipantTeam;
import com.example.playgroundmanage.vo.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchParticipantTeamRepository extends JpaRepository<MatchParticipantTeam, Long> {

    List<MatchParticipantTeam> findAllByTeam(Team team);
}
