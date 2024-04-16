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

    private final GameSorting gameSorting;

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
    public List<UsersGameDto.UsersGameResponseDto> getUserHostGamesInMonth(User user, LocalDateTime localDateTime) {
        List<Game> games = gameRepository.findAllByHost(user);

        List<Game> gamesInSameYearMonth = gameFinder.getGamesForYearMonth(games, localDateTime);

        List<Game> gamesOrderedByLatest = gameSorting.sortGamesByOldest(gamesInSameYearMonth);

        return gamesOrderedByLatest.stream()
                .map(gameDtoConverter::toUsersGameResponseDto)
                .toList();
    }
    @Transactional
    public List<UsersGameDto.UsersGameResponseDto> getUserHostGames(User user) {
        List<Game> games = gameRepository.findAllByHost(user);

        List<Game> gamesOrderedByLatest = gameSorting.sortGamesByOldest(games);

        return gamesOrderedByLatest.stream()
                .map(gameDtoConverter::toUsersGameResponseDto)
                .toList();
    }

    @Transactional
    public List<UsersGameDto.UsersGameResponseDto> getUserJoinGames(UsersGameDto.UsersGameRequestDto usersGameRequestDto) {
        List<GameParticipant> usersGameParticipants = gameParticipantRepository.findAllByUser(usersGameRequestDto.getUser());

        List<Game> userJoinGames = usersGameParticipants.stream()
                .map(GameParticipant::getGame)
                .toList();

        List<Game> userJoinGamesInSameYearMonth = gameFinder.getGamesForYearMonth(userJoinGames, usersGameRequestDto.getMyDateTime().getLocalDateTime());

        return userJoinGamesInSameYearMonth.stream()
                .map(gameDtoConverter::toUsersGameResponseDto)
                .toList();
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
