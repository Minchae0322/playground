package com.example.playgroundmanage.althlectis.service;

import com.example.playgroundmanage.althlectis.dto.request.AthleticsDetailsRequest;
import com.example.playgroundmanage.althlectis.dto.response.AthleticsDetailsResponse;
import com.example.playgroundmanage.althlectis.dto.response.GameResponse;
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
    public GameResponse getGameInfo(Long gameId) {
        Athletics athletics = athleticsRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        return GameResponse.of(athletics);
    }

    public AthleticsDetailsResponse getAthleticsDetails(AthleticsDetailsRequest athleticsDetailsRequest) {
        Athletics athletics = athleticsRepository.findById(athleticsDetailsRequest.gameId())
                .orElseThrow(GameNotExistException::new);
        /*if (athletics instanceof RankAthletics) {
            return AthleticsDetailsResponse.of(((RankAthletics) athletics).getAthleticsSides().stream()
                    .filter(athleticsSide -> athleticsSide.getGameTeamSide().equals(GameTeamSide.fromString(athleticsDetailsRequest.gameTeamSide())))
                    .findFirst().orElseThrow());
        }*/
            return AthleticsDetailsResponse.of((FriendlyAthletics) athletics);
    }

    private Athletics getAthleticsByType(Athletics athletics) {
        if (athletics instanceof RankAthletics) {
            return (RankAthletics) athletics;
        }
        return (FriendlyAthletics) athletics;
    }
}
