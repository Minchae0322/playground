package com.example.playgroundmanage.game.repository;

import com.example.playgroundmanage.game.vo.impl.SoloGameJoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SoloGameJoinRequestRepository extends JpaRepository<SoloGameJoinRequest, Long> {
}
