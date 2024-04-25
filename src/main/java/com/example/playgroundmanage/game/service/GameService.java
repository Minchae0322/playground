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



    private final GameDtoConverter gameDtoConverter;


    @Transactional
    public GameResponseDto getGameInfo(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        return gameDtoConverter.toGameResponse(game);
    }

    @Transactional
    public void deleteGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        game.getPlayground().getGames().remove(game);

        //아마도 game 에서 CompetingGame -> SubTeam -> List<GameParticipant>
        //이런식으로 먼저 삭제되어서 game -> List<GameParticipant> 를 못찾는듯

        List<GameParticipant> gameParticipants = game.getGameParticipants();
        gameParticipants.forEach(GameParticipant::delete);
        //gameParticipantRepository.deleteAll(gameParticipants);

        gameRepository.delete(game);
    }








}
