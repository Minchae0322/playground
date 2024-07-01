package com.example.playgroundmanage.login.service;

import com.example.playgroundmanage.althlectis.repo.AthleticsParticipantRepository;
import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.UserNotParticipantGameException;
import com.example.playgroundmanage.game.repository.GameParticipantRepository;
import com.example.playgroundmanage.game.repository.GameRepository;
import com.example.playgroundmanage.game.service.GameDtoConverter;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.GameParticipant;
import com.example.playgroundmanage.login.dto.UserGameInfoResponse;
import com.example.playgroundmanage.login.dto.UsersGameDto;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.util.GameFinder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserJoinGameService {

    private final AthleticsParticipantRepository athleticsParticipantRepository;

    private final GameFinder gameFinder;

    private final GameDtoConverter gameDtoConverter;

    private final AthleticsRepository athleticsRepository;

    @Transactional
    public List<UserGameInfoResponse> getUserJoinGames(UsersGameDto.UsersGameRequestDto usersGameRequestDto) {
        List<AthleticsParticipant> usersGameParticipants = athleticsParticipantRepository.findAllByUser(usersGameRequestDto.getUser());

        return getAthleticsForYearMonth(usersGameParticipants, usersGameRequestDto.getMyDateTime().getLocalDateTime())
                .stream()
                .map(athleticsParticipant -> UserGameInfoResponse.of(athleticsParticipant.getAthletics()))
                .toList();
    }

    private List<AthleticsParticipant> getAthleticsForYearMonth(List<AthleticsParticipant> athleticsParticipants, LocalDateTime localDateTime ) {
        return Optional.ofNullable(athleticsParticipants)
                .orElse(Collections.emptyList())
                .stream()
                .filter(athleticsParticipant -> isGameOnYearMonth(athleticsParticipant, localDateTime))
                .toList();
    }

    private boolean isGameOnYearMonth(AthleticsParticipant athleticsParticipant, LocalDateTime localDateTime) {
        int targetYear = localDateTime.getYear();
        int targetMonth = localDateTime.getMonthValue();
        if (athleticsParticipant.getGameStartDateTime() == null) {
            athleticsParticipant.updateGameStartDate(athleticsParticipant.getAthletics().getGameStartDateTime());
        }
        return athleticsParticipant.getGameStartDateTime().getYear() == targetYear && athleticsParticipant.getGameStartDateTime().getMonthValue() == targetMonth;
    }

  /*  @Transactional
    public void userOutOfGame(Long gameId, User user) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);

        GameParticipant gameParticipant = gameParticipantRepository.findByGameAndUser(game, user)
                .orElseThrow(UserNotParticipantGameException::new);

        game.getGameParticipants().remove(gameParticipant);

        gameParticipantRepository.delete(gameParticipant);

    }
*/

}
