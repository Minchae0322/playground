package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.game.vo.Team;
import com.example.playgroundmanage.game.vo.Teaming;
import com.example.playgroundmanage.game.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamingRepository extends JpaRepository<Teaming, Long> {
    List<Teaming> findAllByTeam(Team team);

    List<Teaming> findAllByUser(User user);
}
