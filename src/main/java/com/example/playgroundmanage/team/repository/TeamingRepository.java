package com.example.playgroundmanage.team.repository;

import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.team.vo.Teaming;
import com.example.playgroundmanage.login.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamingRepository extends JpaRepository<Teaming, Long> {
    List<Teaming> findAllByTeam(Team team);

    List<Teaming> findAllByUser(User user);

    Teaming findByTeamAndUser(Team team, User user);
}
