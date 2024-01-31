package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class TeamRequestDto {

    private String teamName;

    private MultipartFile teamPic;

    private User leader;

    private SportsEvent sportsEvent;

    private String teamDescription;

    @Builder
    public TeamRequestDto(String teamName, MultipartFile teamPic, User leader, SportsEvent sportsEvent, String teamDescription) {
        validate(teamName, teamPic, sportsEvent);
        this.teamName = teamName;
        this.teamPic = teamPic;
        this.leader = leader;
        this.sportsEvent = sportsEvent;
        this.teamDescription = teamDescription;
    }

    private void validate(String teamName, MultipartFile teamPic, SportsEvent sportsEvent) {
        if (teamName.length() < 1 || teamName.length() > 15) {
            throw new IllegalArgumentException("wrong team name");
        }
        if (teamPic == null) {
            throw new IllegalArgumentException("not exist team profile image");
        }
        if (sportsEvent == null) {
            throw new IllegalArgumentException("wrong sports event");
        }
    }
}
