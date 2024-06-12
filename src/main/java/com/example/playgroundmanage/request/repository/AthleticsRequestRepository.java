package com.example.playgroundmanage.request.repository;

import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.vo.AthleticsRequest;
import com.example.playgroundmanage.request.vo.impl.athletics.FriendlyAthleticsJoinRequest;
import com.example.playgroundmanage.request.vo.impl.athletics.RankAthleticsJoinRequest;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.RequestState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AthleticsRequestRepository extends JpaRepository<AthleticsRequest, Long> {

    Optional<AthleticsRequest> findByUserAndAthletics(User user, Athletics athletics);

    List<FriendlyAthleticsJoinRequest> findAllByHostAndRequestState(User host, RequestState requestState);

    List<RankAthleticsJoinRequest> findAllByRequestStateAndHost(RequestState requestState, User host);
}
