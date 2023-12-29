package com.example.playgroundmanage.location.repository;

import com.example.playgroundmanage.location.vo.Playground;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaygroundRepository extends JpaRepository<Playground, Long> {
}
