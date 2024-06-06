package com.example.playgroundmanage.request.repository;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.AthleticsRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AthleticsRequestRepository extends JpaRepository<AthleticsRequest, Long> {

    Optional<AthleticsRequest> findByUserAndAthletics(User user, Athletics athletics);
}
