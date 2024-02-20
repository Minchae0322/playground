package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.GameDto;

import com.example.playgroundmanage.dto.UsersGameDto;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.MatchNotExistException;
import com.example.playgroundmanage.exception.UserNotParticipantGameException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.util.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static com.example.playgroundmanage.util.LocationFormatter.getLocation;


@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    private final GameSorting gameSorting;

    private final GameFinder gameFinder;

    private final CompetingTeamRepository competingTeamRepository;

    private final GameParticipantRepository gameParticipantRepository;

    private final GameParticipantFinder gameParticipantFinder;

    @Transactional
    public List<UsersGameDto.UsersGameResponseDto> getGamesUserHostByDate(User user, LocalDateTime localDateTime) {
        List<Game> games = gameRepository.findAllByHost(user);
        List<Game> gamesByDate = gameFinder.getGamesForYearMonth(games, localDateTime);
        List<Game> gamesOrderedByLatest = gameSorting.sortGamesByOldest(gamesByDate);

        return gamesOrderedByLatest.stream()
                .map(game -> UsersGameDto.UsersGameResponseDto.builder()
                        .gameId(game.getId())
                        .playgroundId(game.getPlayground().getId())
                        .location(getLocation(game))
                        .gameType(game.getGameType())
                        .localDateStartTime(game.getGameStartDateTime())
                        .gameStart(DateFormat.dateFormatYYYYMMDDHHMM(game.getGameStartDateTime()))
                        .hostName(game.getHost().getNickname())
                        .gameName(game.getGameName())
                        .runningTime(game.getRunningTime())
                        .build())
                .toList();
    }
    @Transactional
    public List<UsersGameDto.UsersGameResponseDto> getGamesUserHost(User user) {
        List<Game> games = gameRepository.findAllByHost(user);
        List<Game> gamesOrderedByLatest = gameSorting.sortGamesByOldest(games);

        return gamesOrderedByLatest.stream()
                .map(game -> UsersGameDto.UsersGameResponseDto.builder()
                        .gameId(game.getId())
                        .playgroundId(game.getPlayground().getId())
                        .location(getLocation(game))
                        .gameType(game.getGameType())
                        .localDateStartTime(game.getGameStartDateTime())
                        .gameStart(DateFormat.dateFormatYYYYMMDDHHMM(game.getGameStartDateTime()))
                        .hostName(game.getHost().getNickname())
                        .gameName(game.getGameName())
                        .runningTime(game.getRunningTime())
                        .build())
                .toList();
    }

    @Transactional
    public void deleteGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        game.getPlayground().getGames().remove(game);
        List<GameParticipant> gameParticipants = game.getGameParticipants();
        gameParticipants.forEach(GameParticipant::delete);

        gameRepository.delete(game);
    }

    @Transactional
    public void deleteCompetingTeam(CompetingTeam competingTeam) {
        competingTeamRepository.delete(competingTeam);
    }

    @Transactional
    public GameDto getGameInfo(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        return game.toGameDto();
    }

    @Transactional
    public void userOutOfGame(Long gameId, User user) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        GameParticipant gameParticipant = gameParticipantFinder.getParticipantInGame(game, user)
                .orElseThrow(UserNotParticipantGameException::new);
        game.getGameParticipants().remove(gameParticipant);

        gameParticipantRepository.delete(gameParticipant);

    }

    @Transactional
    public List<UsersGameDto.UsersGameResponseDto> getMonthGameAsc(UsersGameDto.UsersGameRequestDto usersGameRequestDto) {
        List<GameParticipant> userGame = gameParticipantRepository.findAllByUser(usersGameRequestDto.getUser());
        List<GameParticipant> sameMonthGameParticipants = gameParticipantFinder.getGameOfSameYearAndMonth(userGame, usersGameRequestDto.getMyDateTime().getLocalDateTime());

        return sameMonthGameParticipants.stream()
                .sorted(Comparator.comparing(gameParticipant -> gameParticipant.getGame().getGameStartDateTime()))
                .map(GameParticipant::toUsersGameResponseDto)
                .toList();
    }



}
