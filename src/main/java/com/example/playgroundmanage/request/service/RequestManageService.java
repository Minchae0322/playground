package com.example.playgroundmanage.request.service;

import com.example.playgroundmanage.game.repository.GameRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestManageService {

    private final GameRequestRepository gameRequestRepository;

  /*  @Transactional
    public List<RequestInfoDto> getAllPendingRequestByHost(User host) {
        List<GameRequest> gameRequests = gameRequestRepository.findAllByHostAndExpiredTimeAfter(host, LocalDateTime.now());

        return gameRequests.stream()
                .map(GameRequest::toGameRequestInfoDto)
                .toList();
    }*/
}
