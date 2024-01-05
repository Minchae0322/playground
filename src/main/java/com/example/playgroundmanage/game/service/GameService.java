package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.GameDto;

import com.example.playgroundmanage.dto.SubTeamDto;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.MatchNotExistException;
import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.location.repository.PlaygroundRepository;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.store.InMemoryMultipartFile;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.location.vo.Playground;
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


    private final FileHandler fileHandler;
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

    public SubTeamDto getSoloTeamByTeamSide(Long gameId, MatchTeamSide matchTeamSide) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(GameNotExistException::new);
        SubTeam soloTeam = game.getCompetingTeamBySide(matchTeamSide).getSoloTeam();

        return SubTeamDto.builder()
                .users(soloTeam.getParticipantsInfo())
                .build();
    }

    private SubTeamDto toSubTeamDto(SubTeam subTeam){
        InMemoryMultipartFile teamProfileImg = fileHandler.extractFile(subTeam.getTeam().getTeamPic());

        return SubTeamDto.builder()
                .teamId(subTeam.getTeam().getId())
                .teamName(subTeam.getTeam().getTeamName())
                .teamProfileImg(teamProfileImg)
                .users(subTeam.getParticipantsInfo())
                .build();
    }








    public Game getMatch(Long matchId) {
        return gameRepository.findById(matchId).orElseThrow(MatchNotExistException::new);
    }



}
