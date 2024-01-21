package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.request.vo.TeamRequest;
import com.example.playgroundmanage.game.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRequestRepository extends JpaRepository<TeamRequest, Long> {

    Optional<TeamRequest> findByTeamAndUser(Team team, User user);

    List<TeamRequest> findAllByLeader(User leader);
}
