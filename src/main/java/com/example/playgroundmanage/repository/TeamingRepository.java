package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.Team;
import com.example.playgroundmanage.vo.Teaming;
import com.example.playgroundmanage.vo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamingRepository extends JpaRepository<Teaming, Long> {
    List<Teaming> findAllByTeam(Team team);

    List<Teaming> findAllByUser(User user);
}
