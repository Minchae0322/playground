package com.example.playgroundmanage.location.repository;

import com.example.playgroundmanage.location.vo.Campus;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.type.SportsEvent;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;
import java.util.Optional;

public interface PlaygroundRepository extends JpaRepository<Playground, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "120")})
    Optional<Playground> findById(Long id);
    List<Playground> findAllByCampusAndSportsEvent(Campus campus, SportsEvent sportsEvent);

}
