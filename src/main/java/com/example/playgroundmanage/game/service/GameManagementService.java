package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.game.repository.GameRequestRepository;
import com.example.playgroundmanage.game.repository.GameParticipantRepository;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.game.vo.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GameManagementService {

    private final GameRequestRepository gameRequestRepository;


    private final GameParticipantRepository gameParticipantRepository;

    public void deletePreviousRequest(Game game, User user) {
        gameRequestRepository.findByGameAndUser(game, user)
                .ifPresent(gameRequestRepository::delete);
    }

    public List<GameParticipant> findGameParticipantsInGame(Game game) {
        return gameParticipantRepository.findAllByGame(game);
    }
}
