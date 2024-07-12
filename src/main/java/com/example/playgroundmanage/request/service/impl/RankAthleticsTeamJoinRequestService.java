package com.example.playgroundmanage.request.service.impl;


import com.example.playgroundmanage.althlectis.repo.AthleticsParticipantRepository;
import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.dto.AthleticsJoinRequest;
import com.example.playgroundmanage.request.dto.PendingRequestResponse;
import com.example.playgroundmanage.request.repository.AthleticsRequestRepository;
import com.example.playgroundmanage.request.service.AthleticsRequestService;
import com.example.playgroundmanage.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankAthleticsTeamJoinRequestService implements AthleticsRequestService {

    private final UserRepository userRepository;

    private final AthleticsRepository athleticsRepository;

    private final AthleticsRequestRepository athleticsRequestRepository;

    private final TeamRepository teamRepository;

    private final AthleticsParticipantRepository athleticsParticipantRepository;

    @Override
    public String getRequestType() {
        return "rankGameTeamJoin";
    }
    @Override
    public Long generateRequest(Long userId, AthleticsJoinRequest athleticsJoinRequest) {
        User requestUser = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);


        return null;
    }



    @Override
    public Long acceptRequest(Long requestId) {
        return null;
    }

    @Override
    public List<PendingRequestResponse> getPendingRequests(Long hostId) {
        return null;
    }

    @Override
    public void rejectRequest(Long requestId) {

    }
}
