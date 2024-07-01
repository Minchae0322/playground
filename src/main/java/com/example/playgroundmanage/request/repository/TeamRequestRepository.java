package com.example.playgroundmanage.request.repository;

import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.TeamRequest;
import com.example.playgroundmanage.request.vo.impl.TeamJoinRequest;
import com.example.playgroundmanage.team.vo.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRequestRepository extends JpaRepository<TeamRequest, Long> {

    Optional<TeamRequest> findByTeamAndUser(Team team, User user);

    List<TeamJoinRequest> findAllByLeader(User leader);
}
