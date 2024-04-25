package com.example.playgroundmanage.login.service;

import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.service.GameDtoConverter;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.login.dto.UsersGameDto;
import com.example.playgroundmanage.login.repository.UserRepository;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.util.GameFinder;
import com.example.playgroundmanage.util.GameSorting;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHostGameService {

    private final GameRepository gameRepository;

    private final GameSorting gameSorting;

    private final GameFinder gameFinder;

    private final GameDtoConverter gameDtoConverter;

    @Transactional
    public List<UsersGameDto.UsersGameResponseDto> getUserHostGames(User user) {
        List<Game> games = gameRepository.findAllByHost(user);

        List<Game> gamesOrderedByLatest = gameSorting.sortGamesByOldest(games);

        return gamesOrderedByLatest.stream()
                .map(gameDtoConverter::toUsersGameResponseDto)
                .toList();
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


}
