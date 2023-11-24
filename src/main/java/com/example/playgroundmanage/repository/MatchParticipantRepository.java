package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.MatchParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {
}
