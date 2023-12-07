package com.example.playgroundmanage.repository;

import com.example.playgroundmanage.vo.Playground;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaygroundRepository extends JpaRepository<Playground, Long> {
}
