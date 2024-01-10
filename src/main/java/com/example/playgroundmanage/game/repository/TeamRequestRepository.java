package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.game.vo.TeamRequest;
import com.example.playgroundmanage.game.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRequestRepository extends JpaRepository<TeamRequest, Long> {

    Optional<TeamRequest> findByTeamAndUser(Team team, User user);
}
