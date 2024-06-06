package com.example.playgroundmanage.request.service.impl;

import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.althlectis.vo.impl.FriendlyAthletics;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.exception.UserNotValidException;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.dto.AthleticsJoinRequest;
import com.example.playgroundmanage.request.repository.AthleticsRequestRepository;
import com.example.playgroundmanage.request.service.AthleticsRequestService;
import com.example.playgroundmanage.request.vo.AthleticsRequest;
import com.example.playgroundmanage.request.vo.impl.athletics.FriendlyAthleticsJoinRequest;
import com.example.playgroundmanage.type.RequestState;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendlyAthleticsJoinAthleticsRequestService implements AthleticsRequestService {

    private final AthleticsRequestRepository athleticsRequestRepository;

    private final AthleticsRepository athleticsRepository;

    private final UserRepository userRepository;

    @Override
    public String getRequestType() {
        return "friendlyGameJoin";
    }
    @Override
    @Transactional
    public Long generateRequest(Long userId, AthleticsJoinRequest athleticsJoinRequest) {
        User requestUser = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        FriendlyAthletics athletics = (FriendlyAthletics) athleticsRepository.findById(athleticsJoinRequest.athleticsId())
                .orElseThrow(GameNotExistException::new);

        if (isUserParticipatedAthletics(athletics.getAthleticsParticipants(), requestUser)) {
            throw new UserNotValidException();
        }

        FriendlyAthleticsJoinRequest friendlyAthleticsJoinRequest = (FriendlyAthleticsJoinRequest) findPreviousRequest(requestUser, athletics)
                .orElse(FriendlyAthleticsJoinRequest.of(
                        athletics,
                        requestUser,
                        athletics.getHost(),
                        getRequestState(requestUser, athletics.getHost())
                ));

        return athleticsRequestRepository.save(friendlyAthleticsJoinRequest).getId();
    }



    private Optional<AthleticsRequest> findPreviousRequest(User user, Athletics athletics) {
        return athleticsRequestRepository.findByUserAndAthletics(user, athletics);
    }

    private RequestState getRequestState(User requestUser, User host) {
        if (requestUser.equals(host)) {
            return RequestState.ACCEPTED;
        }
        return RequestState.PENDING;
    }

    private boolean isUserParticipatedAthletics(List<? extends AthleticsParticipant> participants, User user) {
        return participants.stream()
                .anyMatch(gp -> gp.getUser().equals(user));
    }
}
