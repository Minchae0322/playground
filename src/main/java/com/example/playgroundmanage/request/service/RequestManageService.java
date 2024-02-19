package com.example.playgroundmanage.request.service;

import com.example.playgroundmanage.dto.RequestInfoDto;
import com.example.playgroundmanage.game.repository.GameRequestRepository;
import com.example.playgroundmanage.game.vo.User;
import com.example.playgroundmanage.request.vo.GameRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.RequestInfo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
