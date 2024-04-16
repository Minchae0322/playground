package com.example.playgroundmanage.game.vo;


import com.example.playgroundmanage.login.dto.UserResponseDto;
import com.example.playgroundmanage.login.dto.UsersGameDto;
import com.example.playgroundmanage.login.vo.User;
import com.example.playgroundmanage.type.GameRecord;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.example.playgroundmanage.util.DateFormat.dateFormatYYYYMMDDHHMM;
import static com.example.playgroundmanage.util.LocationFormatter.getLocation;

@Entity
@Getter
@RequiredArgsConstructor
public class GameParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    private SubTeam subTeam;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Game game;

    @Enumerated
    private GameRecord gameRecord;

    private boolean isAccepted;

    @Builder
    public GameParticipant(Long id, User user, SubTeam subTeam, Game game, boolean isAccepted) {
        this.id = id;
        this.user = user;
        this.subTeam = subTeam;
        this.game = game;
        this.isAccepted = isAccepted;
    }



    public void delete() {
        this.game = null;
        this.user = null;
        this.subTeam = null;
    }

    public void updateGameRecord(GameRecord gameRecord) {
        this.gameRecord = gameRecord;
    }

    public UserResponseDto toUserResponseDto() {
        return UserResponseDto.builder()
                .userNickname(user.getNickname())
                .userId(user.getId())
                .userProfileImg(user.getUserProfileImg().getFileUrl())
                .build();
    }




}
