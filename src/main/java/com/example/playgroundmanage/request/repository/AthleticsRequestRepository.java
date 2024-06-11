package com.example.playgroundmanage.request.repository;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.AthleticsRequest;
import com.example.playgroundmanage.request.vo.impl.athletics.FriendlyAthleticsJoinRequest;
import com.example.playgroundmanage.type.RequestState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AthleticsRequestRepository extends JpaRepository<AthleticsRequest, Long> {

    Optional<AthleticsRequest> findByUserAndAthletics(User user, Athletics athletics);

    List<FriendlyAthleticsJoinRequest> findAllByHostAndRequestState(User user, RequestState requestState);
}
