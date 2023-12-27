package com.example.playgroundmanage.game.service;

import com.example.playgroundmanage.dto.GameDto;

import com.example.playgroundmanage.dto.response.SubTeamDto;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.exception.MatchNotExistException;
import com.example.playgroundmanage.exception.PlaygroundNotExistException;
import com.example.playgroundmanage.game.repository.*;
import com.example.playgroundmanage.game.vo.CompetingTeam;
import com.example.playgroundmanage.game.vo.Game;
import com.example.playgroundmanage.game.vo.SubTeam;
import com.example.playgroundmanage.game.match.GameRequestProcess;
import com.example.playgroundmanage.repository.PlaygroundRepository;
import com.example.playgroundmanage.store.FileHandler;
import com.example.playgroundmanage.type.MatchTeamSide;
import com.example.playgroundmanage.util.TeamSelector;
import com.example.playgroundmanage.vo.Playground;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    private final SubTeamRepository subTeamRepository;

    private final PlaygroundRepository playgroundRepository;



    @Transactional
    public Long generateGame(Long playgroundId, GameDto gameDto) {
        Playground playground = playgroundRepository.findById(playgroundId).orElseThrow(PlaygroundNotExistException::new);

        Game game = Game.builder()
                .gameName(gameDto.getGameName())
                .host(gameDto.getHost())
                .playground(playground)
                .gameStartDateTime(gameDto.getMyDateTime().getLocalDateTime())
                .sportsEvent(gameDto.getSportsEvent())
                .runningTime(gameDto.getRunningTime())
                .build();

        return gameRepository.save(game).getId();
    }







    public List<SubTeamDto> getTeamsBySide(Long gameId, MatchTeamSide matchTeamSide) {
        Game game = gameRepository.findById(gameId).orElseThrow(GameNotExistException::new);
        CompetingTeam competingTeam = game.getCompetingTeamBySide(matchTeamSide);

        return competingTeam.getSubTeamsNotSoloTeam().stream()
                .map(t -> {
                    try {
                        return createSubTeamDtoFromSubTeam(t);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }

    private SubTeamDto createSubTeamDtoFromSubTeam(SubTeam subTeam) throws IOException {
        //MultipartFile teamImg = fileHandler.extractFile(subTeam.getTeam().getTeamPic());
        return SubTeamDto.builder()
                .teamId(subTeam.getTeam().getId())
                .teamName(subTeam.getTeam().getTeamName())
                //.teamImg(teamImg)
                .users(subTeam.getParticipantsInfo())
                .build();
    }








    public Game getMatch(Long matchId) {
        return gameRepository.findById(matchId).orElseThrow(MatchNotExistException::new);
    }



}
