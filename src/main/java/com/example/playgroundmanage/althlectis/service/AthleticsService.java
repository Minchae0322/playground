package com.example.playgroundmanage.althlectis.service;

import com.example.playgroundmanage.althlectis.dto.GameResponse;
import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.exception.GameNotExistException;

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
}
