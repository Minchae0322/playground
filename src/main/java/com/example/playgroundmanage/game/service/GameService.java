package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.GameDto;

import com.example.playgroundmanage.dto.SubTeamDto;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.MatchNotExistException;
import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.exception.UserNotParticipantGameException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.vo.*;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.location.vo.Playground;
import com.example.playgroundmanage.util.GameParticipantFinder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.playgroundmanage.util.GameValidation.validateOverlappingGames;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    private final SubTeamRepository subTeamRepository;

    private final PlaygroundRepository playgroundRepository;

    private final UserService userService;

    private final FileHandler fileHandler;

    private final GameParticipantRepository gameParticipantRepository;

    private final GameParticipantFinder gameParticipantFinder;
    @Transactional
    public Long generateGame(Long playgroundId, GameDto gameDto) {
        Playground playground = playgroundRepository.findById(playgroundId)
                .orElseThrow(PlaygroundNotExistException::new);

        validateOverlappingGames(playground.getGames(), gameDto.toGameDateDto());

        Game game = Game.builder()
                .gameName(gameDto.getGameName())
                .host(gameDto.getHost())
                .playground(playground)
                .gameStartDateTime(gameDto.getStartDateTime().getLocalDateTime())
                .sportsEvent(gameDto.getSportsEvent())
                .isFriendly(gameDto.isFriendly())
                .runningTime(gameDto.getRunningTime())
                .build();

        return gameRepository.save(game).getId();
    }


    public List<SubTeamDto> getSubTeamsByTeamSide(Long gameId, MatchTeamSide matchTeamSide) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);
        CompetingTeam competingTeam = game.getCompetingTeamBySide(matchTeamSide);

        return competingTeam.getSubTeamsNotSoloTeam()
                .stream()
                .map(this::toSubTeamDto)
                .toList();
    }

    @Transactional
    public SubTeamDto getSoloTeamByTeamSide(Long gameId, MatchTeamSide matchTeamSide) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);
        SubTeam soloTeam = game.getCompetingTeamBySide(matchTeamSide).getSoloTeam();

        return SubTeamDto.builder()
                .subTeamId(soloTeam.getId())
                .users(soloTeam.getGameParticipants().stream()
                        .map(gameParticipant -> userService.getUserInfo(gameParticipant.getUser()))
                        .toList())
                .build();
    }



    private SubTeamDto toSubTeamDto(SubTeam subTeam){
        InMemoryMultipartFile teamProfileImg = fileHandler.extractFile(subTeam.getTeam().getTeamPic());

        return SubTeamDto.builder()
                .subTeamId(subTeam.getId())
                .teamId(subTeam.getTeam().getId())
                .teamName(subTeam.getTeam().getTeamName())
                .teamProfileImg(teamProfileImg)
                .teamDescription(subTeam.getTeam().getDescription())
                .users(subTeam.getGameParticipants().stream()
                        .map(gameParticipant -> userService.getUserInfoInTeam(subTeam.getTeam(), gameParticipant.getUser()))
                        .toList())
                .build();
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

    private void checkAndDeleteSubTeamIfEmpty(SubTeam subTeam) {
        if (subTeam != null && subTeam.getGameParticipants().size() <= 1) {
            subTeamRepository.delete(subTeam);
        }
    }


    public Game getMatch(Long matchId) {
        return gameRepository.findById(matchId).orElseThrow(MatchNotExistException::new);
    }



}
