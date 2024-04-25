package com.example.playgroundmanage.login.service;

import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.game.repository.GameParticipantRepository;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.service.GameDtoConverter;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.login.dto.UsersGameDto;
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
