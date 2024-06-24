package com.example.playgroundmanage.location.dto.response;

import com.example.playgroundmanage.location.vo.Playground;

import java.time.LocalDateTime;

public record PlaygroundInfoResponse(
        Long playgroundId,

        String playgroundName,

        String sportsEvent,

        String playgroundProfileImg,

        String schoolName,

        String campusName,

        Integer upcomingGameNum
) {
    public static PlaygroundInfoResponse of(Playground playground) {
        return new PlaygroundInfoResponse(
                playground.getId(),
                playground.getName(),
                playground.getSportsEvent().getValue_cn(),
                playground.getImg().getFileUrl(),
                playground.getCampus().getSchool().getSchoolName(),
                playground.getCampus().getCampusName(),
                playground.getAthletics().stream()
                        .filter(athletics -> athletics.getGameStartDateTime().isAfter(LocalDateTime.now()))
                        .toList()
                        .size()
        );
    }

}
