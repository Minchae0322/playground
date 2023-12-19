package com.example.playgroundmanage.dto;

import com.example.playgroundmanage.type.SportsEvent;
import com.example.playgroundmanage.game.vo.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class TeamRegistration {

    private String teamName;

    private MultipartFile teamPic;

    private User leader;

    private SportsEvent sportsEvent;

    private String teamDescription;

    @Builder
    public TeamRegistration(String teamName, MultipartFile teamPic, User leader, SportsEvent sportsEvent, String teamDescription) {
        this.teamName = teamName;
        this.teamPic = teamPic;
        this.leader = leader;
        this.sportsEvent = sportsEvent;
        this.teamDescription = teamDescription;
    }
}
