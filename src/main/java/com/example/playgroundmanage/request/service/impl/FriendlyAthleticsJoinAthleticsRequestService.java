package com.example.playgroundmanage.request.service.impl;

import com.example.playgroundmanage.althlectis.repo.AthleticsParticipantRepository;
import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.althlectis.vo.impl.FriendlyAthletics;
import com.example.playgroundmanage.althlectis.vo.impl.SoloAthleticsParticipant;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.RequestNotExistException;
import com.example.playgroundmanage.exception.UserNotExistException;
import com.example.playgroundmanage.exception.UserNotValidException;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.dto.AthleticsJoinRequest;
import com.example.playgroundmanage.request.dto.PendingRequestResponse;
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

    private final AthleticsParticipantRepository athleticsParticipantRepository;

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

        if (isHostRequest(requestUser, athletics.getHost())) {
            participateInAthletics(requestUser, athletics);
        }

        AthleticsRequest friendlyAthleticsJoinRequest = findPreviousRequest(requestUser, athletics)
                .orElse(FriendlyAthleticsJoinRequest.of(
                        athletics,
                        requestUser,
                        athletics.getHost(),
                        getRequestState(requestUser, athletics.getHost())
                ));

        return athleticsRequestRepository.save(friendlyAthleticsJoinRequest).getId();
    }

    @Override
    public Long acceptRequest(Long requestId) {
        FriendlyAthleticsJoinRequest friendlyAthleticsJoinRequest = (FriendlyAthleticsJoinRequest) athleticsRequestRepository.findById(requestId)
                .orElseThrow(RequestNotExistException::new);

        if (isUserParticipatedAthletics(
                friendlyAthleticsJoinRequest.getAthletics().getAthleticsParticipants(),
                friendlyAthleticsJoinRequest.getUser())
        ) {
            throw new UserNotValidException();
        }

        friendlyAthleticsJoinRequest.setRequestState(RequestState.ACCEPTED);

        return participateInAthletics(
                friendlyAthleticsJoinRequest.getUser(),
                (FriendlyAthletics) friendlyAthleticsJoinRequest.getAthletics()
        );

    }

    private Long participateInAthletics(User user, FriendlyAthletics friendlyAthletics) {
        return athleticsParticipantRepository.save(
                SoloAthleticsParticipant.of(
                        user,
                        friendlyAthletics
                )
        ).getId();
    }

    private boolean isHostRequest(User requestUser, User host) {
        return requestUser.equals(host);
    }

    @Override
    public List<PendingRequestResponse> getPendingRequests(Long hostId) {
        User host = userRepository.findById(hostId)
                .orElseThrow(UserNotExistException::new);

        List<FriendlyAthleticsJoinRequest> athleticsRequests = athleticsRequestRepository.findAllByHostAndRequestState(host, RequestState.PENDING);

        return athleticsRequests.stream()
                .map(PendingRequestResponse::of)
                .toList();

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
