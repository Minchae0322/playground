package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.game.dto.GameResponseDto;
import com.example.playgroundmanage.login.dto.UsersGameDto;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.UserNotParticipantGameException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.util.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    private final GameFinder gameFinder;

    private final GameParticipantRepository gameParticipantRepository;

    private final GameDtoConverter gameDtoConverter;


    @Transactional
    public GameResponseDto getGameInfo(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        return gameDtoConverter.toGameResponse(game);
    }






    @Transactional
    public void userOutOfGame(Long gameId, User user) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        GameParticipant gameParticipant = gameParticipantRepository.findByGameAndUser(game, user)
                .orElseThrow(UserNotParticipantGameException::new);

        game.getGameParticipants().remove(gameParticipant);

        gameParticipantRepository.delete(gameParticipant);

    }





}
