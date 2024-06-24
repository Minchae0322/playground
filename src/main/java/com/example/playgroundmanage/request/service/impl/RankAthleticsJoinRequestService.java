package com.example.playgroundmanage.request.service.impl;

import com.example.playgroundmanage.althlectis.repo.AthleticsParticipantRepository;
import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.althlectis.vo.impl.RankAthletics;
import com.example.playgroundmanage.althlectis.vo.impl.SoloAthleticsParticipant;
import com.example.playgroundmanage.althlectis.vo.impl.TeamAthleticsParticipant;
import com.example.playgroundmanage.exception.*;
import com.example.playgroundmanage.team.repository.TeamRepository;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.request.dto.AthleticsJoinRequest;
import com.example.playgroundmanage.request.dto.PendingRequestResponse;
import com.example.playgroundmanage.request.repository.AthleticsRequestRepository;
import com.example.playgroundmanage.request.service.AthleticsRequestService;
import com.example.playgroundmanage.request.vo.AthleticsRequest;
import com.example.playgroundmanage.request.vo.impl.athletics.RankAthleticsJoinRequest;
import com.example.playgroundmanage.team.vo.Team;
import com.example.playgroundmanage.type.GameTeamSide;
import com.example.playgroundmanage.type.RequestState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RankAthleticsJoinRequestService implements AthleticsRequestService {

    private final UserRepository userRepository;

    private final AthleticsRepository athleticsRepository;

    private final AthleticsRequestRepository athleticsRequestRepository;

    private final TeamRepository teamRepository;

    private final AthleticsParticipantRepository athleticsParticipantRepository;

    @Override
    public String getRequestType() {
        return "teamGameJoin";
    }

    @Override
    public Long generateRequest(Long userId, AthleticsJoinRequest athleticsJoinRequest) {
        User requestUser = userRepository.findById(userId)
                .orElseThrow(UserNotExistException::new);

        RankAthletics athletics = (RankAthletics) athleticsRepository.findById(athleticsJoinRequest.athleticsId())
                .orElseThrow(GameNotExistException::new);

        if (isUserParticipatedAthletics(athletics.getAthleticsParticipants(), requestUser)) {
            throw new UserNotValidException();
        }

        if (isHostRequest(requestUser, athletics.getHost())) {
            participateInAthletics(requestUser, athletics, athleticsJoinRequest);
        }

        AthleticsRequest rankAthleticsJoinRequest = makeRequest(requestUser, athletics, athleticsJoinRequest);

        return athleticsRequestRepository.save(rankAthleticsJoinRequest).getId();
    }

    private AthleticsRequest makeRequest(User user, RankAthletics athletics, AthleticsJoinRequest athleticsJoinRequest) {
        if (athleticsJoinRequest.teamId() != null) {
            Team team = teamRepository.findById(athleticsJoinRequest.teamId())
                    .orElseThrow(TeamNotExistException::new);

            return findPreviousRequest(user, athletics)
                    .orElse(RankAthleticsJoinRequest.of(
                            athletics,
                            user,
                            athletics.getHost(),
                            getRequestState(user, athletics.getHost()),
                            team,
                            GameTeamSide.valueOf(athleticsJoinRequest.gameTeamSide())
                    ).updateRankRequest(team));

        }

        return findPreviousRequest(user, athletics)
                .orElse(RankAthleticsJoinRequest.of(
                        athletics,
                        user,
                        athletics.getHost(),
                        getRequestState(user, athletics.getHost())
                ));
    }

    private Long participateInAthletics(User user, RankAthletics rankAthletics, AthleticsJoinRequest athleticsJoinRequest) {
        if (athleticsJoinRequest.teamId() != null) {
            Team team = teamRepository.findById(athleticsJoinRequest.teamId())
                    .orElseThrow(TeamNotExistException::new);
            return athleticsParticipantRepository.save(
                    TeamAthleticsParticipant.of(
                            GameTeamSide.valueOf(athleticsJoinRequest.gameTeamSide()),
                            user,
                            rankAthletics,
                            team
                    )
            ).getId();
        }

        return athleticsParticipantRepository.save(
                SoloAthleticsParticipant.of(
                        GameTeamSide.valueOf(athleticsJoinRequest.gameTeamSide()),
                        user,
                        rankAthletics
                )
        ).getId();

    }
    private boolean isUserParticipatedAthletics(List<? extends AthleticsParticipant> participants, User user) {
        return participants.stream()
                .anyMatch(gp -> gp.getUser().equals(user));
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

    private boolean isHostRequest(User requestUser, User host) {
        return requestUser.equals(host);
    }

    @Override
    public Long acceptRequest(Long requestId) {
        RankAthleticsJoinRequest rankAthleticsJoinRequest = (RankAthleticsJoinRequest) athleticsRequestRepository.findById(requestId)
                .orElseThrow(RequestNotExistException::new);

        if (isUserParticipatedAthletics(
                rankAthleticsJoinRequest.getAthletics().getAthleticsParticipants(),
                rankAthleticsJoinRequest.getUser())
        ) {
            throw new UserNotValidException();
        }

        rankAthleticsJoinRequest.setRequestState(RequestState.ACCEPTED);

        return athleticsParticipantRepository.save(
                TeamAthleticsParticipant.of(
                        rankAthleticsJoinRequest.getGameTeamSide(),
                        rankAthleticsJoinRequest.getUser(),
                        (RankAthletics) rankAthleticsJoinRequest.getAthletics(),
                        rankAthleticsJoinRequest.getTeam()
                )
        ).getId();
    }

    @Override
    public List<PendingRequestResponse> getPendingRequests(Long hostId) {
        User host = userRepository.findById(hostId)
                .orElseThrow(UserNotExistException::new);

        List<RankAthleticsJoinRequest> athleticsRequests = athleticsRequestRepository.findAllByRequestStateAndHost(RequestState.PENDING, host);

        return athleticsRequests.stream()
                .map(PendingRequestResponse::of)
                .toList();
    }

    @Override
    public void rejectRequest(Long requestId) {
        athleticsRequestRepository.findById(requestId)
                .ifPresent(athleticsRequestRepository::delete);
    }

}
