package com.example.playgroundmanage.althlectis.service;

import com.example.playgroundmanage.althlectis.dto.request.GameResultRequest;
import com.example.playgroundmanage.althlectis.repo.AthleticsResultRepository;
import com.example.playgroundmanage.althlectis.repo.AthleticsRepository;
import com.example.playgroundmanage.althlectis.vo.Athletics;
import com.example.playgroundmanage.althlectis.vo.AthleticsParticipant;
import com.example.playgroundmanage.exception.GameNotExistException;
import com.example.playgroundmanage.login.repository.UserAthleticsRecordRepository;

import com.example.playgroundmanage.login.vo.UserAthleticsRecord;
import com.example.playgroundmanage.type.GameRecord;
import com.example.playgroundmanage.type.GameTeamSide;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AthleticsResultService {

    private final AthleticsRepository athleticsRepository;

    private final AthleticsResultRepository athleticsResultRepository;

    private final UserAthleticsRecordRepository userAthleticsRecordRepository;

    public void updateAthleticsResult(GameResultRequest gameResultRequest) {
        Athletics athletics = athleticsRepository.findById(gameResultRequest.gameId())
                .orElseThrow(GameNotExistException::new);

        athleticsResultRepository.save(athletics.getAthleticsResult()
                .update(gameResultRequest.homeScore(), gameResultRequest.awayScore())
        );

        updateAthleticsParticipantRecords(athletics, gameResultRequest);
    }

    private void updateAthleticsParticipantRecords(Athletics athletics, GameResultRequest gameResultRequest) {
        GameTeamSide winSide = determineWinSide(gameResultRequest);

        athletics.getAthleticsParticipants().forEach(participant -> {
                    GameRecord gameRecord = determineGameRecord(winSide, participant.getGameTeamSide());
                    participant.updateAthleticsRecord(gameRecord);
                    updateUserRecord(participant, gameRecord);
                }
        );

        athleticsRepository.save(athletics);
    }

    private void updateUserRecord(AthleticsParticipant athleticsParticipant, GameRecord gameRecord) {
        UserAthleticsRecord userAthleticsRecord = userAthleticsRecordRepository.findByUser(athleticsParticipant.getUser())
                .orElse(UserAthleticsRecord.builder()
                        .user(athleticsParticipant.getUser())
                        .win(0)
                        .draw(0)
                        .lose(0)
                        .none(0)
                        .build());
        userAthleticsRecord.update(gameRecord);
        userAthleticsRecordRepository.save(userAthleticsRecord);
    }

    private GameTeamSide determineWinSide(GameResultRequest gameResultRequest) {
        int homeScore = gameResultRequest.homeScore();
        int awayScore = gameResultRequest.awayScore();

        if (homeScore > awayScore) {
            return GameTeamSide.HOME;
        } else if (awayScore > homeScore) {
            return GameTeamSide.AWAY;
        } else {
            return GameTeamSide.NONE;
        }
    }

    private GameRecord determineGameRecord(GameTeamSide winSide, GameTeamSide myTeamSide) {
        if (winSide.equals(GameTeamSide.NONE)) {
            return GameRecord.DRAW;
        } else if (winSide.equals(myTeamSide)) {
            return GameRecord.WIN;
        } else {
            return GameRecord.LOSE;
        }
    }

}
