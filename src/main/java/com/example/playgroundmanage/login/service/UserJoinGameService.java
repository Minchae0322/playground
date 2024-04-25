package com.example.playgroundmanage.login.service;

import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.UserNotParticipantGameException;
import com.example.playgroundmanage.game.repository.GameParticipantRepository;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.service.GameDtoConverter;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.login.dto.UsersGameDto;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.util.GameFinder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserJoinGameService {

    private final GameParticipantRepository gameParticipantRepository;

    private final GameFinder gameFinder;

    private final GameDtoConverter gameDtoConverter;

    private final GameRepository gameRepository;

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
    public void userOutOfGame(Long gameId, User user) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        GameParticipant gameParticipant = gameParticipantRepository.findByGameAndUser(game, user)
                .orElseThrow(UserNotParticipantGameException::new);

        game.getGameParticipants().remove(gameParticipant);

        gameParticipantRepository.delete(gameParticipant);

    }


}
