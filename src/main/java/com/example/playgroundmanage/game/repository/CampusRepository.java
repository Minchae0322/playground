package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.location.vo.Campus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampusRepository extends JpaRepository<Campus, Long> {
}
