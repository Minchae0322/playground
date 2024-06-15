package com.example.playgroundmanage.althlectis.service;

import com.example.playgroundmanage.althlectis.dto.request.AthleticsDetailsRequest;
import com.example.playgroundmanage.althlectis.dto.response.AthleticsDetailsResponse;
import com.example.playgroundmanage.althlectis.dto.response.AthleticsResponse;
import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.impl.FriendlyAthletics;
import com.example.playgroundmanage.althlectis.vo.impl.RankAthletics;
import com.example.playgroundmanage.exception.GameNotExistException;

import com.example.playgroundmanage.type.GameTeamSide;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AthleticsService {

    private final AthleticsRepository athleticsRepository;

    @Transactional
    public AthleticsResponse getGameInfo(Long gameId) {
        Athletics athletics = athleticsRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        return AthleticsResponse.of(athletics);
    }

    public AthleticsDetailsResponse getFriendlyAthleticsDetails(AthleticsDetailsRequest athleticsDetailsRequest) {
        FriendlyAthletics athletics = (FriendlyAthletics) athleticsRepository.findById(athleticsDetailsRequest.gameId())
                .orElseThrow(GameNotExistException::new);

        return AthleticsDetailsResponse.of(athletics);
    }

    public AthleticsDetailsResponse getRankAthleticsDetails(AthleticsDetailsRequest athleticsDetailsRequest) {
        RankAthletics athletics = (RankAthletics) athleticsRepository.findById(athleticsDetailsRequest.gameId())
                .orElseThrow(GameNotExistException::new);

        return AthleticsDetailsResponse.of(
                athletics,
                GameTeamSide.valueOf(athleticsDetailsRequest.gameTeamSide())
        );
    }

}
