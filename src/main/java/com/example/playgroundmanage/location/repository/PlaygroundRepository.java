package com.example.playgroundmanage.location.repository;

import com.example.playgroundmanage.location.vo.Playground;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import java.util.Optional;

public interface PlaygroundRepository extends JpaRepository<Playground, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Playground> findById(Long id);

}
