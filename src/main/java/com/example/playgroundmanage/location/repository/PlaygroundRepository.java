package com.example.playgroundmanage.location.repository;

import com.example.playgroundmanage.location.vo.Campus;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.type.SportsEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaygroundRepository extends JpaRepository<Playground, Long> {
    List<Playground> findAllByCampusAndSportsEvent(Campus campus, SportsEvent sportsEvent);
}
